package studying.diplom.retailhub.data.repositories

import io.ktor.http.HttpStatusCode
import studying.diplom.retailhub.data.data_sources.LocalSource
import studying.diplom.retailhub.data.data_sources.RemoteSource
import studying.diplom.retailhub.data.data_sources.api.ApiException
import studying.diplom.retailhub.data.mappers.toApiEntity
import studying.diplom.retailhub.data.mappers.toDbEntity
import studying.diplom.retailhub.data.mappers.toModel
import studying.diplom.retailhub.domain.models.qr_code.QrCodeModel
import studying.diplom.retailhub.domain.models.shop.DepartmentModel
import studying.diplom.retailhub.domain.models.shop.StoreModel
import studying.diplom.retailhub.domain.repositories.StoreRepository

class StoreRepositoryImpl(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource
) : StoreRepository {

    override suspend fun addStore(store: StoreModel): Result<Unit> {
        return remoteSource.addStore(store.toApiEntity()).onSuccess {
            localSource.saveStore(store.toDbEntity())
        }
    }

    override suspend fun getMyStore(): Result<StoreModel> {
        return remoteSource.getMyStore().fold(
            onSuccess = { apiEntity ->
                localSource.saveStore(apiEntity.toDbEntity())
                Result.success(apiEntity.toModel())
            },
            onFailure = { exception ->
                if (exception is ApiException && (exception.statusCode == HttpStatusCode.NotFound || exception.statusCode == HttpStatusCode.BadRequest)) {
                    localSource.clearStore()
                    Result.failure(exception)
                } else {
                    val localData = localSource.getStore()
                    if (localData != null) Result.success(localData.toModel()) 
                    else Result.failure(exception)
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
            onFailure = { exception ->
                if (exception is ApiException && (exception.statusCode == HttpStatusCode.NotFound || exception.statusCode == HttpStatusCode.BadRequest)) {
                    localSource.clearDepartments()
                    Result.success(emptyList())
                } else {
                    val localData = localSource.getDepartments()
                    if (localData.isNotEmpty()) Result.success(localData.map { it.toModel() })
                    else Result.failure(exception)
                }
            }
        )
    }

	override suspend fun getDepartment(id: String): Result<DepartmentModel> {
		return remoteSource.getDepartment(id).fold(
			onSuccess = { apiEntity ->
				localSource.saveDepartment(apiEntity.toDbEntity())
				Result.success(apiEntity.toModel())
			},
			onFailure = { exception ->
				if (exception is ApiException && (exception.statusCode == HttpStatusCode.NotFound || exception.statusCode == HttpStatusCode.BadRequest)) {
                    localSource.deleteDepartment(id)
                    Result.failure(exception)
                } else {
                    val localData = localSource.getDepartment(id)
                    if (localData != null) Result.success(localData.toModel())
                    else Result.failure(exception)
                }
			}
		)
	}

	override suspend fun updateDepartment(department: DepartmentModel): Result<DepartmentModel> {
        return remoteSource.updateDepartment(department.toApiEntity()).map { apiEntity ->
            localSource.saveDepartment(apiEntity.toDbEntity())
            apiEntity.toModel()
        }
    }

    override suspend fun deleteDepartment(department: DepartmentModel): Result<Unit> {
        return remoteSource.deleteDepartment(department.toApiEntity()).onSuccess {
            localSource.deleteDepartment(department.id)
        }
    }

    override suspend fun postQrCode(departmentId: String, label: String): Result<QrCodeModel> {
        return remoteSource.postQrCode(departmentId, label).map { it.toModel() }
    }

    override suspend fun getQrCodes(departmentId: String): Result<List<QrCodeModel>> {
        return remoteSource.getQrCodes(departmentId).map { list -> list.map { it.toModel() } }
    }

    override suspend fun deleteQrCode(qrCodeId: String): Result<Unit> {
        return remoteSource.deleteQrCode(qrCodeId)
    }

    override suspend fun downloadQrCode(qrCodeId: String): Result<ByteArray> {
        return remoteSource.downloadQrCode(qrCodeId)
    }
}
