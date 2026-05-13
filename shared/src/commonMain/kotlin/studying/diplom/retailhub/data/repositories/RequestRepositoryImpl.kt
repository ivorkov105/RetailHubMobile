package studying.diplom.retailhub.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import studying.diplom.retailhub.data.data_sources.LocalSource
import studying.diplom.retailhub.data.data_sources.RemoteSource
import studying.diplom.retailhub.data.data_sources.api.WSService
import studying.diplom.retailhub.data.mappers.toApiEntity
import studying.diplom.retailhub.data.mappers.toDbEntity
import studying.diplom.retailhub.data.mappers.toModel
import studying.diplom.retailhub.data.paging.RequestRemoteMediator
import studying.diplom.retailhub.domain.models.request.RequestModel
import studying.diplom.retailhub.domain.models.request.RequestStatus
import studying.diplom.retailhub.domain.repositories.RequestRepository

class RequestRepositoryImpl(
	private val remoteSource: RemoteSource,
	private val localSource: LocalSource,
    private val wsService: WSService
) : RequestRepository {

	override suspend fun addRequests(requests: List<RequestModel>): Result<Unit> {
		localSource.addRequests(requests.map { it.toDbEntity() })
		return remoteSource.addRequests(requests.map { it.toApiEntity() })
	}

	override suspend fun assignRequest(requestId: String): Result<RequestModel> {
		val remoteResult = remoteSource.assignRequest(requestId)

		return remoteResult.fold(
			onSuccess = { apiEntity ->
				localSource.addRequests(listOf(apiEntity.toDbEntity()))
				Result.success(apiEntity.toModel())
			},
			onFailure = { Result.failure(it) }
		)
	}

	override suspend fun completeRequest(requestId: String): Result<RequestModel> {
		val remoteResult = remoteSource.completeRequest(requestId)

		return remoteResult.fold(
			onSuccess = { apiEntity ->
				localSource.addRequests(listOf(apiEntity.toDbEntity()))
				Result.success(apiEntity.toModel())
			},
			onFailure = { Result.failure(it) }
		)
	}

    override suspend fun getRequests(
        status: String?,
        departmentId: String?,
        dateFrom: String?,
        dateTo: String?,
        page: Int,
        size: Int
    ): Result<List<RequestModel>> {
        return remoteSource.getRequests(status, departmentId, dateFrom, dateTo, page, size).map { listEntity ->
            listEntity.content.map { it.toModel() }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedRequests(
        status: RequestStatus?,
        departmentId: String?,
        dateFrom: String?,
        dateTo: String?
    ): Flow<PagingData<RequestModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            remoteMediator = RequestRemoteMediator(
	            remoteSource = remoteSource,
	            localSource = localSource,
	            status = status?.name,
	            departmentId = departmentId,
	            dateFrom = dateFrom,
	            dateTo = dateTo
            ),
            pagingSourceFactory = { 
                localSource.getRequestsPaged(
                    status = status?.name ?: "",
                    departmentId = departmentId ?: "",
                    dateFrom = dateFrom ?: "",
                    dateTo = dateTo ?: ""
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toModel() }
        }
    }

	override suspend fun saveRequest(request: RequestModel) {
		localSource.addRequests(listOf(request.toDbEntity()))
	}

    override fun observeRequestUpdates(): Flow<RequestModel> {
        return wsService.requestUpdates.map { it.toModel() }
    }

    override fun connectToWebSocket() {
        wsService.connect()
    }

    override fun disconnectFromWebSocket() {
        wsService.disconnect()
    }

    override fun subscribeToStore(storeId: String) {
        wsService.subscribeToStore(storeId)
    }

    override fun subscribeToDepartment(departmentId: String) {
        wsService.subscribeToDepartment(departmentId)
    }

    override fun unsubscribeFromStore(storeId: String) {
        wsService.unsubscribeFromStore(storeId)
    }

    override fun unsubscribeFromDepartment(departmentId: String) {
        wsService.unsubscribeFromDepartment(departmentId)
    }
}
