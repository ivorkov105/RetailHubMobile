package studying.diplom.retailhub.presentation.notifications

import studying.diplom.retailhub.domain.models.notifications.NotificationModel

data class NotificationsState(
    val notifications: List<NotificationModel> = emptyList(),
    val expandedNotificationId: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
