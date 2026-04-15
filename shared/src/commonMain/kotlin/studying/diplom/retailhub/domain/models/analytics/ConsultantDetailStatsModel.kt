package studying.diplom.retailhub.domain.models.analytics

import studying.diplom.retailhub.domain.models.request.RequestModel

data class ConsultantDetailStatsModel(
    val stats: ConsultantStatsModel,
    val recentRequests: List<RequestModel>
)
