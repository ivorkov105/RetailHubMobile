package studying.diplom.retailhub.domain.use_cases.analytics_use_cases

import studying.diplom.retailhub.domain.models.analytics.ConsultantDetailStatsModel
import studying.diplom.retailhub.domain.repositories.AnalyticsRepository

class GetConsultantDetailStatsUseCase(
    private val repository: AnalyticsRepository
) {
    suspend operator fun invoke(userId: String): Result<ConsultantDetailStatsModel> = 
        repository.getConsultantDetailStats(userId)
}
