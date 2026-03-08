package studying.diplom.retailhub.presentation.main.store.create_store

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
import studying.diplom.retailhub.domain.use_cases.store_use_cases.AddStoreUseCase
import studying.diplom.retailhub.domain.use_cases.store_use_cases.UpdateMyStoreUseCase

sealed class CreateStoreNavigationEvent {
    object NavigateBack : CreateStoreNavigationEvent()
}

class CreateStoreViewModel(
    private val addStoreUseCase: AddStoreUseCase,
    private val updateMyStoreUseCase: UpdateMyStoreUseCase,
    private val initialStore: StoreModel? = null
) : ScreenModel {

    private val _state = MutableStateFlow(
        CreateStoreState(
            name = initialStore?.name ?: "",
            address = initialStore?.address ?: "",
        )
    )
    val state: StateFlow<CreateStoreState> = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<CreateStoreNavigationEvent>()
    val navigationEvents: SharedFlow<CreateStoreNavigationEvent> = _navigationEvents.asSharedFlow()

    fun onEvent(event: CreateStoreEvent) {
        when (event) {
            is CreateStoreEvent.OnNameChange -> _state.update { it.copy(name = event.value) }
            is CreateStoreEvent.OnAddressChange -> _state.update { it.copy(address = event.value) }
            is CreateStoreEvent.OnDescriptionChange -> _state.update { it.copy(description = event.value) }
            is CreateStoreEvent.OnSaveClick -> saveStore()
            is CreateStoreEvent.OnErrorDismiss -> _state.update { it.copy(error = null) }
            is CreateStoreEvent.OnBackClick -> {
                screenModelScope.launch { _navigationEvents.emit(CreateStoreNavigationEvent.NavigateBack) }
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
            
            val storeToSave = StoreModel(
                id = initialStore?.id ?: "",
                name = currentState.name,
                address = currentState.address,
                timezone = initialStore?.timezone ?: "Europe/Moscow"
            )

            val result = if (initialStore == null) {
                addStoreUseCase(storeToSave)
            } else {
                updateMyStoreUseCase(storeToSave).map { Unit }
            }

            result.onSuccess {
                _state.update { it.copy(isLoading = false, isSuccess = true) }
                _navigationEvents.emit(CreateStoreNavigationEvent.NavigateBack)
            }.onFailure { throwable ->
                _state.update { it.copy(
                    isLoading = false,
                    error = throwable.message ?: "Ошибка при сохранении"
                ) }
            }
        }
    }
}
