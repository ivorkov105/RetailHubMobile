package studying.diplom.retailhub.presentation.main.store.my_store

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
import studying.diplom.retailhub.domain.use_cases.shop_use_cases.GetMyStoreUseCase

sealed class MyStoreNavigationEvent {
    object NavigateBack : MyStoreNavigationEvent()
}

class MyStoreViewModel(
    private val getMyStoreUseCase: GetMyStoreUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(MyStoreState())
    val state: StateFlow<MyStoreState> = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<MyStoreNavigationEvent>()
    val navigationEvents: SharedFlow<MyStoreNavigationEvent> = _navigationEvents.asSharedFlow()

    fun onEvent(event: MyStoreEvent) {
        when (event) {
            is MyStoreEvent.LoadStore -> loadStore()
            is MyStoreEvent.OnBackClick -> {
                screenModelScope.launch {
                    _navigationEvents.emit(MyStoreNavigationEvent.NavigateBack)
                }
            }
        }
    }

    private fun loadStore() {
        screenModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getMyStoreUseCase().onSuccess { store ->
                _state.update { it.copy(store = store, isLoading = false) }
            }.onFailure { throwable ->
                _state.update { it.copy(
                    isLoading = false,
                    error = throwable.message ?: "Ошибка при загрузке данных магазина"
                ) }
            }
        }
    }
}
