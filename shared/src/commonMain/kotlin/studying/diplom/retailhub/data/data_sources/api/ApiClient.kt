package studying.diplom.retailhub.data.data_sources.api

import studying.diplom.retailhub.data.enteties.auth.LoginRequestEntity
import studying.diplom.retailhub.data.enteties.auth.TokenEntity
import studying.diplom.retailhub.data.enteties.request.RequestEntity
import studying.diplom.retailhub.data.enteties.shop.DepartmentEntity
import studying.diplom.retailhub.data.enteties.shop.StoreEntity
import studying.diplom.retailhub.data.enteties.user.UserEntity

interface ApiClient {

	//Авторизация
    suspend fun login(request: LoginRequestEntity): Result<TokenEntity>
    suspend fun refreshToken(refreshToken: String): Result<TokenEntity>
    suspend fun getMe(): Result<UserEntity>

	//Заявки
    suspend fun getRequests(): Result<List<RequestEntity>>
    suspend fun addRequests(newRequests: List<RequestEntity>): Result<Unit>

	//Магазины и отделы
	suspend fun addStore(newStore: StoreEntity): Result<Unit>
	suspend fun getMyStore(): Result<StoreEntity>
	suspend fun updateMyStore(updatingStore: StoreEntity): Result<StoreEntity>
	suspend fun addDepartment(newDepartment: DepartmentEntity): Result<Unit>
	suspend fun getMyStoreDepartments(): Result<List<DepartmentEntity>>
	suspend fun updateDepartment(updatingDepartment: DepartmentEntity): Result<DepartmentEntity>
	suspend fun deleteDepartment(deletingDepartment: DepartmentEntity): Result<Unit>
}
