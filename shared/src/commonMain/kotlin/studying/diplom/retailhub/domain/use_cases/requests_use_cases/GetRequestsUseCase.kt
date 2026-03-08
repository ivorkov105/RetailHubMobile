package studying.diplom.retailhub.domain.use_cases.requests_use_cases

import studying.diplom.retailhub.domain.models.request.RequestModel
import studying.diplom.retailhub.domain.repositories.RequestRepository

class GetRequestsUseCase(
    private val requestRepository: RequestRepository
) {
    suspend operator fun invoke(
        status: String = "",
        departmentId: String = "",
        dateFrom: String = "",
        dateTo: String = "",
        page: Int = 0,
        size: Int = 20
    ): Result<List<RequestModel>> {
        return requestRepository.getRequests(status, departmentId, dateFrom, dateTo, page, size)
    }
}
