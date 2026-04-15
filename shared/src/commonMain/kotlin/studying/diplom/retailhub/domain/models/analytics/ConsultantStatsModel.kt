package studying.diplom.retailhub.domain.models.analytics

data class ConsultantStatsModel(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val completedRequests: Int,
    val avgResponseTime: Long,
    val rating: Double
)
