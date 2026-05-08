package studying.diplom.retailhub.data.entities.analytics

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConsultantStatsEntity(
    @SerialName("user_id")
    val userId: String = "",
    @SerialName("first_name")
    val firstName: String = "",
    @SerialName("last_name")
    val lastName: String = "",
    @SerialName("total_requests")
    val totalRequests: Int = 0,
    @SerialName("completed_requests")
    val completedRequests: Int = 0,
    @SerialName("avg_reaction_seconds")
    val avgReactionSeconds: Double = 0.0,
    @SerialName("avg_service_seconds")
    val avgServiceSeconds: Double = 0.0,
    @SerialName("total_work_minutes")
    val totalWorkMinutes: Int = 0,
    @SerialName("total_busy_minutes")
    val totalBusyMinutes: Int = 0,
    @SerialName("total_idle_minutes")
    val totalIdleMinutes: Int = 0
)
