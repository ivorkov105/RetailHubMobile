package studying.diplom.retailhub.data.entities.notifications

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationEntity(
    val id: String,
    val title: String,
    val body: String,
    @SerialName("is_read")
    val isRead: Boolean,
    @SerialName("created_at")
    val createdAt: String
)
