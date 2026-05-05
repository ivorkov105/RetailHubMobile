package studying.diplom.retailhub.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import studying.diplom.retailhub.data.data_sources.LocalSource
import studying.diplom.retailhub.data.data_sources.RemoteSource
import studying.diplom.retailhub.data.data_sources.api.WSService
import studying.diplom.retailhub.data.mappers.toApiEntity
import studying.diplom.retailhub.data.mappers.toDbEntity
import studying.diplom.retailhub.data.mappers.toModel
import studying.diplom.retailhub.domain.models.request.RequestModel
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
