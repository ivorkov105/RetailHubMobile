package studying.diplom.retailhub.data.repositories

import studying.diplom.retailhub.data.data_sources.LocalSource
import studying.diplom.retailhub.data.data_sources.RemoteSource
import studying.diplom.retailhub.data.mappers.toApiEntity
import studying.diplom.retailhub.data.mappers.toDbEntity
import studying.diplom.retailhub.data.mappers.toModel
import studying.diplom.retailhub.domain.models.shop.DepartmentModel
import studying.diplom.retailhub.domain.models.shop.StoreModel
import studying.diplom.retailhub.domain.repositories.StoreRepository

class StoreRepositoryImpl(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource
) : StoreRepository {

    override suspend fun addStore(store: StoreModel): Result<Unit> {
        localSource.saveStore(store.toDbEntity())
        return remoteSource.addStore(store.toApiEntity())
    }

    override suspend fun getMyStore(): Result<StoreModel> {
        return remoteSource.getMyStore().fold(
            onSuccess = { apiEntity ->
                localSource.saveStore(apiEntity.toDbEntity())
                Result.success(apiEntity.toModel())
            },
            onFailure = {
                val localData = localSource.getStore()
                if (localData != null) {
                    Result.success(localData.toModel())
                } else {
                    Result.failure(it)
                }
            }
        )
    }

    override suspend fun updateMyStore(store: StoreModel): Result<StoreModel> {
        return remoteSource.updateMyStore(store.toApiEntity()).map { apiEntity ->
            localSource.saveStore(apiEntity.toDbEntity())
            apiEntity.toModel()
        }
    }

    override suspend fun addDepartment(department: DepartmentModel): Result<Unit> {
        return remoteSource.addDepartment(department.toApiEntity())
    }

    override suspend fun getDepartments(): Result<List<DepartmentModel>> {
        return remoteSource.getMyStoreDepartments().fold(
            onSuccess = { apiEntities ->
                localSource.saveDepartments(apiEntities.map { it.toDbEntity() })
                Result.success(apiEntities.map { it.toModel() })
            },
            onFailure = {
                val localData = localSource.getDepartments()
                if (localData.isNotEmpty()) {
                    Result.success(localData.map { it.toModel() })
                } else {
                    Result.failure(it)
                }
            }
        )
    }

    override suspend fun updateDepartment(department: DepartmentModel): Result<DepartmentModel> {
        return remoteSource.updateDepartment(department.toApiEntity()).map { apiEntity ->
            apiEntity.toModel()
        }
    }

    override suspend fun deleteDepartment(department: DepartmentModel): Result<Unit> {
        localSource.deleteDepartment(department.id)
        return remoteSource.deleteDepartment(department.toApiEntity())
    }
}
