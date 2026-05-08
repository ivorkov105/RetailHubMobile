package studying.diplom.retailhub.data.entities.analytics

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConsultantDetailStatsEntity(
    @SerialName("consultant")
    val consultant: ConsultantStatsEntity = ConsultantStatsEntity(),
    @SerialName("daily_breakdown")
    val dailyBreakdown: List<DailyBreakdownEntity> = emptyList()
)

@Serializable
data class DailyBreakdownEntity(
    @SerialName("date")
    val date: String = "",
    @SerialName("requests_completed")
    val requestsCompleted: Int = 0,
    @SerialName("avg_reaction_seconds")
    val avgReactionSeconds: Double = 0.0,
    @SerialName("work_minutes")
    val workMinutes: Int = 0
)
