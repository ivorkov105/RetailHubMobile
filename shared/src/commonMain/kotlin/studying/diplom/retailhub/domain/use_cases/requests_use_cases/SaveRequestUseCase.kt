package studying.diplom.retailhub.domain.use_cases.requests_use_cases

import studying.diplom.retailhub.domain.models.request.RequestModel
import studying.diplom.retailhub.domain.repositories.RequestRepository

class SaveRequestUseCase(
	private val repository: RequestRepository
) {

	suspend operator fun invoke(
		request: RequestModel
	) {
		return repository.saveRequest(request)
	}
}