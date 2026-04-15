package studying.diplom.retailhub.data.enteties.notifications

import kotlinx.serialization.Serializable

@Serializable
data class NotificationListEntity(
    val content: List<NotificationEntity>
)
