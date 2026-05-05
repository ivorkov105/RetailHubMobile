package studying.diplom.retailhub.presentation.notifications

sealed class NotificationsEvent {
    object RefreshNotifications : NotificationsEvent()
    data class ToggleExpand(val notificationId: String) : NotificationsEvent()
    data class MarkAsRead(val notificationId: String) : NotificationsEvent()
}
