package studying.diplom.retailhub.presentation.auth

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import studying.diplom.retailhub.data.data_sources.api.ApiException
import studying.diplom.retailhub.domain.models.devices.DeviceModel
import studying.diplom.retailhub.domain.use_cases.auth_use_cases.GetProfileUseCase
import studying.diplom.retailhub.domain.use_cases.auth_use_cases.LoginUseCase
import studying.diplom.retailhub.domain.use_cases.notifications_use_cases.RegisterDeviceUseCase
import studying.diplom.retailhub.domain.utils.PushTokenProvider

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val registerDeviceUseCase: RegisterDeviceUseCase,
    private val pushTokenProvider: PushTokenProvider
) : ScreenModel {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnPhoneNumberChange -> {
                _state.update { it.copy(phoneNumber = event.value) }
            }

            is AuthEvent.OnPasswordChange -> {
                _state.update { it.copy(password = event.value) }
            }

            is AuthEvent.OnLoginClick -> {
                login()
            }

            is AuthEvent.OnErrorDismiss -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    private fun login() {
        val currentState = _state.value
        if (currentState.phoneNumber.isBlank() || currentState.password.isBlank()) {
            _state.update { it.copy(error = "Пожалуйста, заполните все поля") }
            return
        }

        screenModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            loginUseCase(
                phoneNumber = currentState.phoneNumber,
                password = currentState.password
            ).onSuccess {
                getProfileUseCase().onSuccess { user ->
                    // После успешного входа и получения профиля регистрируем устройство для пушей
                    registerDevice()
                    
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            role = user.role
                        )
                    }
                }.onFailure { error ->
                    val message = if (error is ApiException && error.statusCode == HttpStatusCode.Unauthorized) {
                        "Сессия истекла, войдите снова"
                    } else {
                        error.message ?: "Ошибка при получении профиля"
                    }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = message
                        )
                    }
                }
            }.onFailure { error ->
                val message = if (error is ApiException && error.statusCode == HttpStatusCode.Unauthorized) {
                    "Неверный логин или пароль"
                } else {
                    error.message ?: "Произошла ошибка при входе"
                }
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = message
                    )
                }
            }
        }
    }

    private fun registerDevice() {
        screenModelScope.launch {
            val token = pushTokenProvider.getPushToken()
            if (token != null) {
                registerDeviceUseCase(
                    DeviceModel(
                        fcmToken = token,
                        deviceInfo = pushTokenProvider.getDeviceInfo()
                    )
                )
            }
        }
    }
}
