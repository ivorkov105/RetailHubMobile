package studying.diplom.retailhub.domain.repositories

import studying.diplom.retailhub.domain.models.analytics.AnalyticsDashboardModel
import studying.diplom.retailhub.domain.models.analytics.ConsultantDetailStatsModel
import studying.diplom.retailhub.domain.models.analytics.ConsultantStatsModel
import studying.diplom.retailhub.domain.models.analytics.Period
import studying.diplom.retailhub.domain.models.request.RequestModel

interface AnalyticsRepository {
    suspend fun getDashboard(period: Period): Result<AnalyticsDashboardModel>
    
    suspend fun getConsultantsStats(
        dateFrom: String,
        dateTo: String
    ): Result<List<ConsultantStatsModel>>
    
    suspend fun getConsultantDetailStats(
        userId: String,
        dateFrom: String,
        dateTo: String
    ): Result<ConsultantDetailStatsModel>
    
    suspend fun getRequestsHistory(
        status: String? = null,
        departmentId: String? = null,
        assignedUserId: String? = null,
        dateFrom: String? = null,
        dateTo: String? = null,
        page: Int = 0,
        size: Int = 20
    ): Result<List<RequestModel>>
}
