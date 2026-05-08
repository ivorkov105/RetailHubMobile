package studying.diplom.retailhub.domain.models.analytics

enum class AnalyticsMetric(val label: String) {
    TOTAL_REQUESTS("Всего заявок"),
    COMPLETED_REQUESTS("Выполнено сегодня"),
    ACTIVE_CONSULTANTS("Активные консультанты"),
    AVG_REACTION_TIME("Среднее время реакции"),
    AVG_SERVICE_TIME("Среднее время обслуживания"),
    WORK_MINUTES("Минуты работы"),
    BUSY_MINUTES("Минуты занятости"),
    IDLE_MINUTES("Минуты простоя")
}
