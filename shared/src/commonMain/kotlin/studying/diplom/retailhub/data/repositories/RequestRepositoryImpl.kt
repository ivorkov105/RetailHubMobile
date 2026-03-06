package studying.diplom.retailhub.data.repositories

import studying.diplom.retailhub.data.data_sources.LocalSource
import studying.diplom.retailhub.data.data_sources.RemoteSource
import studying.diplom.retailhub.data.mappers.toDbEntity
import studying.diplom.retailhub.data.mappers.toModel
import studying.diplom.retailhub.data.mappers.toApiEntity
import studying.diplom.retailhub.domain.models.request.RequestModel
import studying.diplom.retailhub.domain.repositories.RequestRepository

class RequestRepositoryImpl(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource
) : RequestRepository {

    override suspend fun getRequests(): Result<List<RequestModel>> {
        val remoteResult = remoteSource.getRequests()
        
        return remoteResult.fold(
            onSuccess = { apiEntities ->
                val dbEntities = apiEntities.map { it.toDbEntity() }
                localSource.updateRequests(dbEntities)
                
                Result.success(apiEntities.map { it.toModel() })
            },
            onFailure = { 
                val localData = localSource.getRequests()
                if (localData.isNotEmpty()) {
                    Result.success(localData.map { it.toModel() })
                } else {
                    Result.failure(it)
                }
            }
        )
    }

    override suspend fun addRequests(requests: List<RequestModel>): Result<Unit> {
        localSource.addRequests(requests.map { it.toDbEntity() })
        return remoteSource.addRequests(requests.map { it.toApiEntity() })
    }
}
