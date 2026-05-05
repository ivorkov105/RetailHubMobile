package studying.diplom.retailhub.presentation.main.notifications

sealed class NotificationsEvent {
    object LoadNotifications : NotificationsEvent()
    object RefreshNotifications : NotificationsEvent()
    data class MarkAsRead(val notificationId: String) : NotificationsEvent()
    data class ToggleExpand(val notificationId: String) : NotificationsEvent()
}
