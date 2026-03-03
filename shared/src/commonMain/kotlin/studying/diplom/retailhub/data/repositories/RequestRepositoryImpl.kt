package studying.diplom.retailhub.data.repositories

import studying.diplom.retailhub.data.data_sources.LocalSource
import studying.diplom.retailhub.data.data_sources.RemoteSource
import studying.diplom.retailhub.data.mappers.toModel
import studying.diplom.retailhub.data.mappers.toEntity
import studying.diplom.retailhub.domain.models.RequestModel
import studying.diplom.retailhub.domain.repositories.RequestRepository

class RequestRepositoryImpl(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource
) : RequestRepository {

    override suspend fun getRequests(): Result<List<RequestModel>> {
        val remoteResult = remoteSource.getRequests()
        
        return remoteResult.fold(
            onSuccess = { entities ->
                val domainModels = entities.map { it.toModel() }
                localSource.updateRequests(domainModels)
                Result.success(domainModels)
            },
            onFailure = { 
                val localData = localSource.getRequests()
                if (localData.isNotEmpty()) {
                    Result.success(localData)
                } else {
                    Result.failure(it)
                }
            }
        )
    }

    override suspend fun addRequests(requests: List<RequestModel>): Result<Unit> {
        localSource.addRequests(requests)
        val entities = requests.map { it.toEntity() }
        return remoteSource.addRequests(entities)
    }
}
