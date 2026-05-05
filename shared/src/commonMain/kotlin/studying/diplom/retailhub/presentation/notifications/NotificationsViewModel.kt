package studying.diplom.retailhub.presentation.notifications

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import studying.diplom.retailhub.domain.use_cases.notifications_use_cases.GetNotificationsUseCase
import studying.diplom.retailhub.domain.use_cases.notifications_use_cases.MarkNotificationReadUseCase
import studying.diplom.retailhub.domain.use_cases.notifications_use_cases.RefreshNotificationsUseCase

class NotificationsViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val refreshNotificationsUseCase: RefreshNotificationsUseCase,
    private val markNotificationReadUseCase: MarkNotificationReadUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(NotificationsState())
    val state: StateFlow<NotificationsState> = _state.asStateFlow()

    init {
        observeNotifications()
        onEvent(NotificationsEvent.RefreshNotifications)
    }

    private fun observeNotifications() {
        getNotificationsUseCase()
            .onEach { notifications ->
                _state.update { it.copy(notifications = notifications) }
            }
            .launchIn(screenModelScope)
    }

    fun onEvent(event: NotificationsEvent) {
        when (event) {
            NotificationsEvent.RefreshNotifications -> {
                screenModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    refreshNotificationsUseCase()
                    _state.update { it.copy(isLoading = false) }
                }
            }
            is NotificationsEvent.ToggleExpand -> {
                val isCurrentlyExpanded = _state.value.expandedNotificationId == event.notificationId
                _state.update { 
                    it.copy(expandedNotificationId = if (isCurrentlyExpanded) null else event.notificationId) 
                }
                if (!isCurrentlyExpanded) {
                    markAsRead(event.notificationId)
                }
            }
            is NotificationsEvent.MarkAsRead -> markAsRead(event.notificationId)
        }
    }

    private fun markAsRead(id: String) {
        screenModelScope.launch {
            markNotificationReadUseCase(id)
        }
    }
}
