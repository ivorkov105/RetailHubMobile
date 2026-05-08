package studying.diplom.retailhub.presentation.main

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import studying.diplom.retailhub.data.data_sources.LocalSource
import studying.diplom.retailhub.domain.use_cases.notifications_use_cases.GetNotificationsUseCase
import studying.diplom.retailhub.presentation.main.employees.employees_list.EmployeesTab
import studying.diplom.retailhub.presentation.main.requests.RequestsTab
import studying.diplom.retailhub.presentation.main.utils.UserRoles

class MainViewModel(
    private val userRole: String? = null,
    private val localSource: LocalSource,
    private val getNotificationsUseCase: GetNotificationsUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(
        MainState(
            currentTab = if (userRole == UserRoles.CONSULTANT.name) RequestsTab else EmployeesTab
        )
    )
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        observeNotifications()
    }

    private fun observeNotifications() {
        getNotificationsUseCase()
            .onEach { notifications ->
                val hasUnread = notifications.any { it.isRead }
                _state.update { it.copy(hasUnreadNotifications = hasUnread) }
            }
            .launchIn(screenModelScope)
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.SelectTab -> {
                _state.update { it.copy(currentTab = event.tab) }
            }
            MainEvent.OnCloseApp, MainEvent.OnLogout -> {}
            MainEvent.OnNotificationsClick -> {}
        }
    }

    override fun onDispose() {
        localSource.clearAllExceptSession()

        super.onDispose()
    }
}
