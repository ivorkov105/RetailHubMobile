package studying.diplom.retailhub.data.repositories

import studying.diplom.retailhub.data.data_sources.RemoteSource
import studying.diplom.retailhub.data.mappers.toModel
import studying.diplom.retailhub.domain.models.analytics.AnalyticsDashboardModel
import studying.diplom.retailhub.domain.models.analytics.ConsultantDetailStatsModel
import studying.diplom.retailhub.domain.models.analytics.ConsultantStatsModel
import studying.diplom.retailhub.domain.models.analytics.Period
import studying.diplom.retailhub.domain.models.request.RequestModel
import studying.diplom.retailhub.domain.repositories.AnalyticsRepository

class AnalyticsRepositoryImpl(
    private val remoteSource: RemoteSource
) : AnalyticsRepository {

    override suspend fun getDashboard(period: Period): Result<AnalyticsDashboardModel> {
        return remoteSource.getAnalyticsDashboard(period.name).map { it.toModel() }
    }

    override suspend fun getConsultantsStats(
        dateFrom: String,
        dateTo: String
    ): Result<List<ConsultantStatsModel>> {
        return remoteSource.getConsultantsStats(dateFrom, dateTo).map { list -> 
            list.map { it.toModel() } 
        }
    }

    override suspend fun getConsultantDetailStats(
        userId: String,
        dateFrom: String,
        dateTo: String
    ): Result<ConsultantDetailStatsModel> {
        return remoteSource.getConsultantDetailStats(userId, dateFrom, dateTo).map { it.toModel() }
    }

    override suspend fun getRequestsHistory(
        status: String?,
        departmentId: String?,
        assignedUserId: String?,
        dateFrom: String?,
        dateTo: String?,
        page: Int,
        size: Int
    ): Result<List<RequestModel>> {
        return remoteSource.getRequestsHistory(
            status, departmentId, assignedUserId, dateFrom, dateTo, page, size
        ).map { list ->
            list.content.map { it.toModel() }
        }
    }
}
