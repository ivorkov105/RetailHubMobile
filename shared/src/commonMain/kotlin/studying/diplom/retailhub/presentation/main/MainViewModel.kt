package studying.diplom.retailhub.presentation.main

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ScreenModel {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.SelectTab -> {
                _state.update { it.copy(currentTab = event.tab) }
            }
        }
    }
}
