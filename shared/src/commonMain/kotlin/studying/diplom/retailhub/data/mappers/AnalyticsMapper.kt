package studying.diplom.retailhub.data.mappers

import studying.diplom.retailhub.data.entities.analytics.AnalyticsDashboardEntity
import studying.diplom.retailhub.data.entities.analytics.ConsultantDetailStatsEntity
import studying.diplom.retailhub.data.entities.analytics.ConsultantStatsEntity
import studying.diplom.retailhub.domain.models.analytics.AnalyticsDashboardModel
import studying.diplom.retailhub.domain.models.analytics.ConsultantDetailStatsModel
import studying.diplom.retailhub.domain.models.analytics.ConsultantStatsModel

fun AnalyticsDashboardEntity.toModel() = AnalyticsDashboardModel(
    totalRequests = totalRequests,
    completedRequests = completedRequests,
    avgCompletionTime = avgCompletionTime,
    activeConsultants = activeConsultants
)

fun ConsultantStatsEntity.toModel() = ConsultantStatsModel(
    userId = userId,
    firstName = firstName,
    lastName = lastName,
    completedRequests = completedRequests,
    avgResponseTime = avgResponseTime,
    rating = rating
)

fun ConsultantDetailStatsEntity.toModel() = ConsultantDetailStatsModel(
    stats = stats.toModel(),
    recentRequests = recentRequests.map { it.toModel() }
)
