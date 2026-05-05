package studying.diplom.retailhub.domain.repositories

import studying.diplom.retailhub.domain.models.user.UserModel

interface UserRepository {
    suspend fun getStoreUsers(): Result<List<UserModel>>
    suspend fun getUser(id: String): Result<UserModel>
    suspend fun addUser(user: UserModel): Result<Unit>
    suspend fun updateUser(user: UserModel): Result<UserModel>
    suspend fun updateUserDepartments(userId: String, departmentIds: List<String>): Result<UserModel>
    suspend fun deleteUser(user: UserModel): Result<Unit>
}
