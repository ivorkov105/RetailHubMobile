package studying.diplom.retailhub.data.repositories

import studying.diplom.retailhub.data.data_sources.RemoteSource
import studying.diplom.retailhub.data.mappers.toModel
import studying.diplom.retailhub.domain.models.analytics.AnalyticsDashboardModel
import studying.diplom.retailhub.domain.models.analytics.ConsultantDetailStatsModel
import studying.diplom.retailhub.domain.models.analytics.ConsultantStatsModel
import studying.diplom.retailhub.domain.models.request.RequestModel
import studying.diplom.retailhub.domain.repositories.AnalyticsRepository

class AnalyticsRepositoryImpl(
    private val remoteSource: RemoteSource
) : AnalyticsRepository {

    override suspend fun getDashboard(): Result<AnalyticsDashboardModel> {
        return remoteSource.getAnalyticsDashboard().map { it.toModel() }
    }

    override suspend fun getConsultantsStats(): Result<List<ConsultantStatsModel>> {
        return remoteSource.getConsultantsStats().map { list -> list.map { it.toModel() } }
    }

    override suspend fun getConsultantDetailStats(userId: String): Result<ConsultantDetailStatsModel> {
        return remoteSource.getConsultantDetailStats(userId).map { it.toModel() }
    }

    override suspend fun getRequestsHistory(
        dateFrom: String,
        dateTo: String,
        page: Int,
        size: Int
    ): Result<List<RequestModel>> {
        return remoteSource.getRequestsHistory(dateFrom, dateTo, page, size).map { list ->
            list.content.map { it.toModel() }
        }
    }
}
