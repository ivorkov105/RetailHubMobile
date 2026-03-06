package studying.diplom.retailhub.domain.use_cases.requests_use_cases

import studying.diplom.retailhub.domain.models.request.RequestModel
import studying.diplom.retailhub.domain.repositories.RequestRepository

class AddRequestsUseCase(
    private val repository: RequestRepository
) {
    suspend operator fun invoke(requests: List<RequestModel>) {
        repository.addRequests(requests)
    }
}
