package studying.diplom.retailhub.data.repositories

import studying.diplom.retailhub.data.data_sources.LocalSource
import studying.diplom.retailhub.data.data_sources.RemoteSource
import studying.diplom.retailhub.data.mappers.toApiEntity
import studying.diplom.retailhub.data.mappers.toDbEntity
import studying.diplom.retailhub.data.mappers.toModel
import studying.diplom.retailhub.domain.models.user.UserModel
import studying.diplom.retailhub.domain.repositories.UserRepository

class UserRepositoryImpl(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource
) : UserRepository {

    override suspend fun getStoreUsers(): Result<List<UserModel>> {
        val remoteResult = remoteSource.getStoreUsers()
        
        return remoteResult.fold(
            onSuccess = { apiEntities ->
                runCatching { localSource.saveUsers(apiEntities.map { it.toDbEntity() }) }
                Result.success(apiEntities.map { it.toModel() })
            },
            onFailure = { error ->
                val localResult = runCatching { 
                    val depts = localSource.getDepartments().map { it.toModel() }
                    localSource.getStoreUsers().map { dbUser ->
                        val ids = if (dbUser.departmentIds.isEmpty()) emptyList() else dbUser.departmentIds.split(",")
                        val userDepts = depts.filter { it.id in ids }
                        UserModel(
                            id = dbUser.id,
                            storeId = dbUser.storeId,
                            phoneNumber = dbUser.phoneNumber,
                            firstName = dbUser.firstName,
                            lastName = dbUser.lastName,
                            role = dbUser.role,
                            currentStatus = dbUser.currentStatus,
                            departments = userDepts,
                            createdAt = dbUser.createdAt
                        )
                    }
                }
                localResult.fold(
                    onSuccess = { localData ->
                        if (localData.isNotEmpty()) {
                            Result.success(localData)
                        } else {
                            Result.failure(error)
                        }
                    },
                    onFailure = { Result.failure(error) }
                )
            }
        )
    }

    override suspend fun getUser(id: String): Result<UserModel> {
        return remoteSource.getUser(id).fold(
            onSuccess = { apiEntity ->
                runCatching { localSource.saveUser(apiEntity.toDbEntity()) }
                Result.success(apiEntity.toModel())
            },
            onFailure = { error ->
                val dbUser = runCatching { localSource.getUser(id) }.getOrNull()
                if (dbUser != null) {
                    val depts = localSource.getDepartments().map { it.toModel() }
                    val ids = if (dbUser.departmentIds.isEmpty()) emptyList() else dbUser.departmentIds.split(",")
                    val userDepts = depts.filter { it.id in ids }
                    Result.success(
                        UserModel(
                            id = dbUser.id,
                            storeId = dbUser.storeId,
                            phoneNumber = dbUser.phoneNumber,
                            firstName = dbUser.firstName,
                            lastName = dbUser.lastName,
                            role = dbUser.role,
                            currentStatus = dbUser.currentStatus,
                            departments = userDepts,
                            createdAt = dbUser.createdAt
                        )
                    )
                } else {
                    Result.failure(error)
                }
            }
        )
    }

    override suspend fun addUser(user: UserModel): Result<Unit> {
        return remoteSource.addUser(user.toApiEntity())
    }

    override suspend fun updateUser(user: UserModel): Result<UserModel> {
        return remoteSource.updateUser(user.toApiEntity()).map { apiEntity ->
            runCatching { localSource.saveUser(apiEntity.toDbEntity()) }
            apiEntity.toModel()
        }
    }

    override suspend fun deleteUser(user: UserModel): Result<Unit> {
        runCatching { localSource.deleteUser(user.id) }
        return remoteSource.deleteUser(user.toApiEntity())
    }
}
