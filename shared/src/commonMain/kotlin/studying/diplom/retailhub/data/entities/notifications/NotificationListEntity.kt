package studying.diplom.retailhub.data.entities.notifications

import kotlinx.serialization.Serializable

@Serializable
data class NotificationListEntity(
    val content: List<NotificationEntity>
)
