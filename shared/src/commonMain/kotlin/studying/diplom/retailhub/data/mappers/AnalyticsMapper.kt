package studying.diplom.retailhub.data.mappers

import studying.diplom.retailhub.data.entities.analytics.AnalyticsDashboardEntity
import studying.diplom.retailhub.data.entities.analytics.ConsultantDetailStatsEntity
import studying.diplom.retailhub.data.entities.analytics.ConsultantStatsEntity
import studying.diplom.retailhub.data.entities.analytics.DailyBreakdownEntity
import studying.diplom.retailhub.domain.models.analytics.AnalyticsDashboardModel
import studying.diplom.retailhub.domain.models.analytics.ConsultantDetailStatsModel
import studying.diplom.retailhub.domain.models.analytics.ConsultantStatsModel
import studying.diplom.retailhub.domain.models.analytics.DailyBreakdownModel

fun AnalyticsDashboardEntity.toModel() = AnalyticsDashboardModel(
    totalRequestsToday = totalRequestsToday,
    completedRequestsToday = completedRequestsToday,
    activeConsultants = activeConsultants,
    avgReactionTimeSeconds = avgReactionTimeSeconds,
    avgServiceTimeSeconds = avgServiceTimeSeconds
)

fun ConsultantStatsEntity.toModel() = ConsultantStatsModel(
    userId = userId,
    firstName = firstName,
    lastName = lastName,
    totalRequests = totalRequests,
    completedRequests = completedRequests,
    avgReactionSeconds = avgReactionSeconds,
    avgServiceSeconds = avgServiceSeconds,
    totalWorkMinutes = totalWorkMinutes,
    totalBusyMinutes = totalBusyMinutes,
    totalIdleMinutes = totalIdleMinutes
)

fun DailyBreakdownEntity.toModel() = DailyBreakdownModel(
    date = date,
    requestsCompleted = requestsCompleted,
    avgReactionSeconds = avgReactionSeconds,
    workMinutes = workMinutes
)

fun ConsultantDetailStatsEntity.toModel() = ConsultantDetailStatsModel(
    consultant = consultant.toModel(),
    dailyBreakdown = dailyBreakdown.map { it.toModel() }
)
