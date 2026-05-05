package studying.diplom.retailhub.data.entities.analytics

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import studying.diplom.retailhub.data.entities.request.RequestEntity

@Serializable
data class ConsultantDetailStatsEntity(
    val stats: ConsultantStatsEntity,
    @SerialName("recent_requests")
    val recentRequests: List<RequestEntity>
)
