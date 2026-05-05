package studying.diplom.retailhub.presentation.main.notifications

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
        refreshNotifications()
    }

    private fun observeNotifications() {
        getNotificationsUseCase()
            .onEach { notifications ->
                _state.update { it.copy(notifications = notifications) }
            }
            .launchIn(screenModelScope)
    }

    private fun refreshNotifications() {
        screenModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            refreshNotificationsUseCase()
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun onEvent(event: NotificationsEvent) {
        when (event) {
            NotificationsEvent.LoadNotifications -> refreshNotifications()
            NotificationsEvent.RefreshNotifications -> refreshNotifications()
            is NotificationsEvent.MarkAsRead -> markAsRead(event.notificationId)
            is NotificationsEvent.ToggleExpand -> toggleExpand(event.notificationId)
        }
    }

    private fun markAsRead(id: String) {
        screenModelScope.launch {
            markNotificationReadUseCase(id)
        }
    }

    private fun toggleExpand(id: String) {
        _state.update { 
            it.copy(expandedNotificationId = if (it.expandedNotificationId == id) null else id)
        }
        // Автоматически помечаем как прочитанное при раскрытии
        markAsRead(id)
    }
}
