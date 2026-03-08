package studying.diplom.retailhub.domain.use_cases.requests_use_cases

import studying.diplom.retailhub.domain.models.request.RequestModel
import studying.diplom.retailhub.domain.repositories.RequestRepository

class CompleteRequestUseCase(
    private val requestRepository: RequestRepository
) {
    suspend operator fun invoke(requestId: String): Result<RequestModel> {
        return requestRepository.completeRequest(requestId)
    }
}
