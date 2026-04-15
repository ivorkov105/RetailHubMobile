package studying.diplom.retailhub.domain.repositories

import studying.diplom.retailhub.domain.models.analytics.AnalyticsDashboardModel
import studying.diplom.retailhub.domain.models.analytics.ConsultantDetailStatsModel
import studying.diplom.retailhub.domain.models.analytics.ConsultantStatsModel
import studying.diplom.retailhub.domain.models.request.RequestModel

interface AnalyticsRepository {
    suspend fun getDashboard(): Result<AnalyticsDashboardModel>
    suspend fun getConsultantsStats(): Result<List<ConsultantStatsModel>>
    suspend fun getConsultantDetailStats(userId: String): Result<ConsultantDetailStatsModel>
    suspend fun getRequestsHistory(dateFrom: String, dateTo: String, page: Int, size: Int): Result<List<RequestModel>>
}
