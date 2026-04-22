package studying.diplom.retailhub.data.repositories

import io.ktor.http.HttpStatusCode
import studying.diplom.retailhub.data.data_sources.LocalSource
import studying.diplom.retailhub.data.data_sources.RemoteSource
import studying.diplom.retailhub.data.data_sources.api.ApiException
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
                localSource.saveUsers(apiEntities.map { it.toDbEntity() })
                Result.success(apiEntities.map { it.toModel() })
            },
            onFailure = { error ->
                if (error is ApiException && error.statusCode == HttpStatusCode.NotFound) {
                    localSource.clearUsers()
                    Result.success(emptyList())
                } else {
                    val localData = runCatching { 
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
                    }.getOrDefault(emptyList())

                    if (localData.isNotEmpty()) Result.success(localData)
                    else Result.failure(error)
                }
            }
        )
    }

    override suspend fun getUser(id: String): Result<UserModel> {
        return remoteSource.getUser(id).fold(
            onSuccess = { apiEntity ->
                localSource.saveUser(apiEntity.toDbEntity())
                Result.success(apiEntity.toModel())
            },
            onFailure = { error ->
                if (error is ApiException && error.statusCode == HttpStatusCode.NotFound) {
                    localSource.deleteUser(id)
                    Result.failure(error)
                } else {
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
            }
        )
    }

    override suspend fun addUser(user: UserModel): Result<Unit> {
        return remoteSource.addUser(user.toApiEntity())
    }

    override suspend fun updateUser(user: UserModel): Result<UserModel> {
        return remoteSource.updateUser(user.toApiEntity()).map { apiEntity ->
            localSource.saveUser(apiEntity.toDbEntity())
            apiEntity.toModel()
        }
    }

    override suspend fun deleteUser(user: UserModel): Result<Unit> {
        return remoteSource.deleteUser(user.toApiEntity()).onSuccess {
            localSource.deleteUser(user.id)
        }
    }
}
