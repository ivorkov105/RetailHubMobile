package studying.diplom.retailhub.presentation.main

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import studying.diplom.retailhub.data.data_sources.LocalSource
import studying.diplom.retailhub.domain.use_cases.shift_use_cases.EndShiftUseCase
import studying.diplom.retailhub.domain.use_cases.shift_use_cases.StartShiftUseCase
import studying.diplom.retailhub.presentation.main.employees.employees_list.EmployeesTab
import studying.diplom.retailhub.presentation.main.requests.RequestsTab
import studying.diplom.retailhub.presentation.main.utils.UserRoles

class MainViewModel(
    private val userRole: String? = null,
    private val startShiftUseCase: StartShiftUseCase,
    private val endShiftUseCase: EndShiftUseCase,
    private val localSource: LocalSource
) : ScreenModel {

    private val _state = MutableStateFlow(
        MainState(
            currentTab = if (userRole == UserRoles.CONSULTANT.name) RequestsTab else EmployeesTab
        )
    )
    val state: StateFlow<MainState> = _state.asStateFlow()

    private fun startShift() {
        screenModelScope.launch {
            startShiftUseCase()
        }
    }

    private fun endShift() {
        screenModelScope.launch {
            endShiftUseCase()
        }
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.SelectTab -> {
                _state.update { it.copy(currentTab = event.tab) }
            }
            MainEvent.OnCloseApp, MainEvent.OnLogout -> {
                if (userRole == UserRoles.CONSULTANT.name) {
                    endShift()
                }
            }
        }
    }

    override fun onDispose() {
        // При закрытии приложения или уничтожении основного экрана очищаем кэш данных
        localSource.clearAllExceptSession()

        super.onDispose()
    }
}
