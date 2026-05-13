package studying.diplom.retailhub.domain.use_cases.analytics_use_cases

import studying.diplom.retailhub.domain.models.analytics.AnalyticsDashboardModel
import studying.diplom.retailhub.domain.models.analytics.Period
import studying.diplom.retailhub.domain.repositories.AnalyticsRepository

class GetAnalyticsDashboardUseCase(
    private val repository: AnalyticsRepository
) {
    suspend operator fun invoke(period: Period): Result<AnalyticsDashboardModel> = repository.getDashboard(period)
}
