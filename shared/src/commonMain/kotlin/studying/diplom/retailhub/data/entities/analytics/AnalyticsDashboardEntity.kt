package studying.diplom.retailhub.data.entities.analytics

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnalyticsDashboardEntity(
    @SerialName("total_requests")
    val totalRequests: Int,
    @SerialName("completed_requests")
    val completedRequests: Int,
    @SerialName("avg_completion_time")
    val avgCompletionTime: Long,
    @SerialName("active_consultants")
    val activeConsultants: Int
)
