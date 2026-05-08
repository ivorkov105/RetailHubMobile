package studying.diplom.retailhub.domain.models.analytics

data class ConsultantStatsModel(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val totalRequests: Int,
    val completedRequests: Int,
    val avgReactionSeconds: Double,
    val avgServiceSeconds: Double,
    val totalWorkMinutes: Int,
    val totalBusyMinutes: Int,
    val totalIdleMinutes: Int
)
