package studying.diplom.retailhub.presentation.main.profile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import studying.diplom.retailhub.domain.models.shop.StoreModel
import studying.diplom.retailhub.domain.use_cases.auth_use_cases.GetProfileUseCase
import studying.diplom.retailhub.domain.use_cases.auth_use_cases.LogoutUseCase
import studying.diplom.retailhub.domain.use_cases.shop_use_cases.GetMyStoreUseCase

sealed class ProfileNavigationEvent {
    object NavigateToAuth : ProfileNavigationEvent()
    object NavigateToMyStore : ProfileNavigationEvent()
    object NavigateToCreateStore : ProfileNavigationEvent()
    data class NavigateToUpdateStore(val store: StoreModel) : ProfileNavigationEvent()
}

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val getMyStoreUseCase: GetMyStoreUseCase,
    private val logoutUseCase: LogoutUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<ProfileNavigationEvent>()
    val navigationEvents: SharedFlow<ProfileNavigationEvent> = _navigationEvents.asSharedFlow()

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.LoadProfile -> loadProfile()
            is ProfileEvent.Logout -> logout()
            is ProfileEvent.OnMyStoreClick -> {
                screenModelScope.launch {
                    _navigationEvents.emit(ProfileNavigationEvent.NavigateToMyStore)
                }
            }
            is ProfileEvent.OnCreateStoreClick -> {
                screenModelScope.launch {
                    _navigationEvents.emit(ProfileNavigationEvent.NavigateToCreateStore)
                }
            }
            is ProfileEvent.OnUpdateStoreClick -> {
                screenModelScope.launch {
                    _state.value.store?.let {
                        _navigationEvents.emit(ProfileNavigationEvent.NavigateToUpdateStore(it))
                    }
                }
            }
        }
    }

    private fun loadProfile() {
        screenModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            val userResult = getProfileUseCase()
            val storeResult = getMyStoreUseCase()

            _state.update { it.copy(
                user = userResult.getOrNull(),
                store = storeResult.getOrNull(),
                isLoading = false
            ) }
        }
    }

    private fun logout() {
        screenModelScope.launch {
            logoutUseCase()
            _navigationEvents.emit(ProfileNavigationEvent.NavigateToAuth)
        }
    }
}
