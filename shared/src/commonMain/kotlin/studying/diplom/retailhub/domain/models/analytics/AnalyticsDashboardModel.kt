package studying.diplom.retailhub.domain.models.analytics

data class AnalyticsDashboardModel(
    val totalRequestsToday: Int,
    val completedRequestsToday: Int,
    val activeConsultants: Int,
    val avgReactionTimeSeconds: Double,
    val avgServiceTimeSeconds: Double
)
