package studying.diplom.retailhub.presentation.main.shop

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
import studying.diplom.retailhub.domain.use_cases.shop_use_cases.AddStoreUseCase
import studying.diplom.retailhub.domain.use_cases.shop_use_cases.GetMyStoreUseCase

sealed class StoreManagementNavigationEvent {
    object NavigateBack : StoreManagementNavigationEvent()
}

class StoreManagementViewModel(
    private val addStoreUseCase: AddStoreUseCase,
    private val getMyStoreUseCase: GetMyStoreUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(StoreManagementState())
    val state: StateFlow<StoreManagementState> = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<StoreManagementNavigationEvent>()
    val navigationEvents: SharedFlow<StoreManagementNavigationEvent> = _navigationEvents.asSharedFlow()

    fun onEvent(event: StoreManagementEvent) {
        when (event) {
            is StoreManagementEvent.OnNameChange -> _state.update { it.copy(name = event.value) }
            is StoreManagementEvent.OnAddressChange -> _state.update { it.copy(address = event.value) }
            is StoreManagementEvent.OnDescriptionChange -> _state.update { it.copy(description = event.value) }
            is StoreManagementEvent.OnSaveClick -> saveStore()
            is StoreManagementEvent.OnErrorDismiss -> _state.update { it.copy(error = null) }
            is StoreManagementEvent.OnBackClick -> {
                screenModelScope.launch { _navigationEvents.emit(StoreManagementNavigationEvent.NavigateBack) }
            }
            is StoreManagementEvent.LoadStore -> loadStore()
        }
    }

    private fun loadStore() {
        screenModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getMyStoreUseCase().onSuccess { store ->
                _state.update { it.copy(
                    name = store.name,
                    address = store.address,
                    isLoading = false
                ) }
            }.onFailure { throwable ->
                _state.update { it.copy(
                    isLoading = false,
                    error = throwable.message ?: "Ошибка при загрузке данных магазина"
                ) }
            }
        }
    }

    private fun saveStore() {
        val currentState = _state.value
        if (currentState.name.isBlank() || currentState.address.isBlank()) {
            _state.update { it.copy(error = "Пожалуйста, заполните обязательные поля") }
            return
        }

        screenModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            val result = addStoreUseCase(
                StoreModel(
                    name = currentState.name,
                    address = currentState.address
                )
            )

            result.onSuccess {
                _state.update { it.copy(isLoading = false, isSuccess = true) }
                _navigationEvents.emit(StoreManagementNavigationEvent.NavigateBack)
            }.onFailure { throwable ->
                _state.update { it.copy(
                    isLoading = false,
                    error = throwable.message ?: "Ошибка при сохранении"
                ) }
            }
        }
    }
}
