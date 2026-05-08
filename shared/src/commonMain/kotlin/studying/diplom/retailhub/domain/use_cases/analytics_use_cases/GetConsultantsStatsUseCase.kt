package studying.diplom.retailhub.domain.use_cases.analytics_use_cases

import studying.diplom.retailhub.domain.models.analytics.ConsultantStatsModel
import studying.diplom.retailhub.domain.repositories.AnalyticsRepository

class GetConsultantsStatsUseCase(
    private val repository: AnalyticsRepository
) {
    suspend operator fun invoke(
        dateFrom: String,
        dateTo: String
    ): Result<List<ConsultantStatsModel>> = repository.getConsultantsStats(dateFrom, dateTo)
}
