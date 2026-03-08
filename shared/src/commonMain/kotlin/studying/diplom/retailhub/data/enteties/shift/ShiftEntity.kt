package studying.diplom.retailhub.data.enteties.shift

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShiftEntity(
    @SerialName("id")
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("user_name")
    val userName: String,
    @SerialName("started_at")
    val startedAt: String,
    @SerialName("ended_at")
    val endedAt: String? = null
)
