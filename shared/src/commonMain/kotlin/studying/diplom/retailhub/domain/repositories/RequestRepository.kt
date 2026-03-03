package studying.diplom.retailhub.domain.repositories

import studying.diplom.retailhub.domain.models.RequestModel

interface RequestRepository {
    suspend fun getRequests(): Result<List<RequestModel>>
    suspend fun addRequests(requests: List<RequestModel>): Result<Unit>
}
