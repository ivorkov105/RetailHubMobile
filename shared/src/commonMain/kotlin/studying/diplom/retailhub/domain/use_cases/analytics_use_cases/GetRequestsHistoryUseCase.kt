package studying.diplom.retailhub.domain.use_cases.analytics_use_cases

import studying.diplom.retailhub.domain.models.request.RequestModel
import studying.diplom.retailhub.domain.repositories.AnalyticsRepository

class GetRequestsHistoryUseCase(
    private val repository: AnalyticsRepository
) {
    suspend operator fun invoke(
        status: String? = null,
        departmentId: String? = null,
        assignedUserId: String? = null,
        dateFrom: String? = null,
        dateTo: String? = null,
        page: Int = 0,
        size: Int = 20
    ): Result<List<RequestModel>> = repository.getRequestsHistory(
        status, departmentId, assignedUserId, dateFrom, dateTo, page, size
    )
}
