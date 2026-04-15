package studying.diplom.retailhub.data.enteties.analytics

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConsultantStatsEntity(
    @SerialName("user_id")
    val userId: String,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("completed_requests")
    val completedRequests: Int,
    @SerialName("avg_response_time")
    val avgResponseTime: Long,
    @SerialName("rating")
    val rating: Double
)
