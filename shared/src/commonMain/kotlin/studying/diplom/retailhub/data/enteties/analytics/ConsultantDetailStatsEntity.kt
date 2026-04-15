package studying.diplom.retailhub.data.enteties.analytics

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import studying.diplom.retailhub.data.enteties.request.RequestEntity

@Serializable
data class ConsultantDetailStatsEntity(
    val stats: ConsultantStatsEntity,
    @SerialName("recent_requests")
    val recentRequests: List<RequestEntity>
)
