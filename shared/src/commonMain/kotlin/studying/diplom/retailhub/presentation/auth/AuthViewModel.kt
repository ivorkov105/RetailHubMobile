package studying.diplom.retailhub.presentation.auth

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import studying.diplom.retailhub.domain.repositories.AuthRepository

class AuthViewModel(
	private val authRepository: AuthRepository
) : ScreenModel {

	private val _state = MutableStateFlow(AuthState())
	val state: StateFlow<AuthState> = _state.asStateFlow()

	fun onEvent(event: AuthEvent) {
		when (event) {
			is AuthEvent.OnPhoneNumberChange -> {
				_state.update { it.copy(phoneNumber = event.value) }
			}

			is AuthEvent.OnPasswordChange    -> {
				_state.update { it.copy(password = event.value) }
			}

			is AuthEvent.OnLoginClick        -> {
				login()
			}

			is AuthEvent.OnErrorDismiss      -> {
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

			authRepository.login(
				phoneNumber = currentState.phoneNumber,
				password = currentState.password
			).onSuccess {
				authRepository.getProfile().onSuccess { user ->
					_state.update {
						it.copy(
							isLoading = false,
							isSuccess = true,
							role = user.role
						)
					}
				}.onFailure { error ->
					_state.update {
						it.copy(
							isLoading = false,
							error = "Ошибка при получении профиля: ${error.message}"
						)
					}
				}
			}.onFailure { error ->
				_state.update {
					it.copy(
						isLoading = false,
						error = error.message ?: "Произошла ошибка при входе"
					)
				}
			}
		}
	}
}
