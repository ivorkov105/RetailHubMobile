package studying.diplom.retailhub.data.entities.analytics

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnalyticsDashboardEntity(
    @SerialName("total_requests_today")
    val totalRequestsToday: Int = 0,
    @SerialName("completed_requests_today")
    val completedRequestsToday: Int = 0,
    @SerialName("active_consultants")
    val activeConsultants: Int = 0,
    @SerialName("avg_reaction_time_seconds")
    val avgReactionTimeSeconds: Double = 0.0,
    @SerialName("avg_service_time_seconds")
    val avgServiceTimeSeconds: Double = 0.0
)
