package studying.diplom.retailhub.domain.models.notifications

data class NotificationModel(
    val id: String,
    val title: String,
    val body: String,
    val isRead: Boolean,
    val createdAt: String
)
