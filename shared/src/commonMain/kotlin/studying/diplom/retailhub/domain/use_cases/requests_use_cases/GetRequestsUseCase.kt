package studying.diplom.retailhub.domain.use_cases.requests_use_cases

import studying.diplom.retailhub.domain.models.request.RequestModel
import studying.diplom.retailhub.domain.repositories.RequestRepository

class GetRequestsUseCase(
    private val requestRepository: RequestRepository
) {
    suspend operator fun invoke(): Result<List<RequestModel>> {
        return requestRepository.getRequests()
    }
}
