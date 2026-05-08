package studying.diplom.retailhub.domain.models.analytics

data class ConsultantDetailStatsModel(
    val consultant: ConsultantStatsModel,
    val dailyBreakdown: List<DailyBreakdownModel>
)

data class DailyBreakdownModel(
    val date: String,
    val requestsCompleted: Int,
    val avgReactionSeconds: Double,
    val workMinutes: Int
)
