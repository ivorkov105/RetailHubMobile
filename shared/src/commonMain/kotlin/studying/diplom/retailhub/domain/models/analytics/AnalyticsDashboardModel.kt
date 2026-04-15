package studying.diplom.retailhub.domain.models.analytics

data class AnalyticsDashboardModel(
    val totalRequests: Int,
    val completedRequests: Int,
    val avgCompletionTime: Long,
    val activeConsultants: Int
)
