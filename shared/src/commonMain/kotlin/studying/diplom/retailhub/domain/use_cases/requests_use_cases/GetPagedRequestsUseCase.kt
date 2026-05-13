package studying.diplom.retailhub.domain.use_cases.requests_use_cases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import studying.diplom.retailhub.domain.models.request.RequestModel
import studying.diplom.retailhub.domain.models.request.RequestStatus
import studying.diplom.retailhub.domain.repositories.RequestRepository

class GetPagedRequestsUseCase(
    private val repository: RequestRepository
) {
    operator fun invoke(
        status: RequestStatus? = null,
        departmentId: String? = null,
        dateFrom: String? = null,
        dateTo: String? = null
    ): Flow<PagingData<RequestModel>> {
        return repository.getPagedRequests(status, departmentId, dateFrom, dateTo)
    }
}
