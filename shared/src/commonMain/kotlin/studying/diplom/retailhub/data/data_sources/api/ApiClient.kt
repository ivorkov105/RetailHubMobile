package studying.diplom.retailhub.data.data_sources.api

import studying.diplom.retailhub.data.enteties.auth.LoginRequestEntity
import studying.diplom.retailhub.data.enteties.auth.TokenEntity
import studying.diplom.retailhub.data.enteties.qr_codes.QREntity
import studying.diplom.retailhub.data.enteties.request.RequestEntity
import studying.diplom.retailhub.data.enteties.request.RequestListEntity
import studying.diplom.retailhub.data.enteties.shift.ShiftEntity
import studying.diplom.retailhub.data.enteties.shop.DepartmentEntity
import studying.diplom.retailhub.data.enteties.shop.StoreEntity
import studying.diplom.retailhub.data.enteties.user.UserEntity

interface ApiClient {

	//Авторизация
    suspend fun login(request: LoginRequestEntity): Result<TokenEntity>
    suspend fun refreshToken(refreshToken: String): Result<TokenEntity>
    suspend fun getMe(): Result<UserEntity>

	//Заявки
    suspend fun getRequests(status: String, departmentId: String, dateFrom: String, dateTo: String, page: Int, size: Int): Result<RequestListEntity>
    suspend fun addRequests(newRequests: List<RequestEntity>): Result<Unit>
    suspend fun assignRequest(requestId: String): Result<RequestEntity>
    suspend fun completeRequest(requestId: String): Result<RequestEntity>

	//Смены
	suspend fun getEmployeesAtShift(): Result<List<ShiftEntity>>
	suspend fun startShift(): Result<ShiftEntity>
	suspend fun getMyShifts(dateFrom: String, dateTo: String): Result<List<ShiftEntity>>
	suspend fun endShift(): Result<ShiftEntity>

	//Магазины и отделы
	suspend fun addStore(newStore: StoreEntity): Result<Unit>
	suspend fun getMyStore(): Result<StoreEntity>
	suspend fun updateMyStore(updatingStore: StoreEntity): Result<StoreEntity>
	
	suspend fun addDepartment(newDepartment: DepartmentEntity): Result<Unit>
	suspend fun getMyStoreDepartments(): Result<List<DepartmentEntity>>
	suspend fun getDepartment(id: String): Result<DepartmentEntity>
	suspend fun updateDepartment(updatingDepartment: DepartmentEntity): Result<DepartmentEntity>
	suspend fun deleteDepartment(deletingDepartment: DepartmentEntity): Result<Unit>

	//Пользователи
	suspend fun getStoreUsers(): Result<List<UserEntity>>
	suspend fun getUser(id: String): Result<UserEntity>
	suspend fun addUser(newUser: UserEntity): Result<Unit>
	suspend fun updateUser(updatingUser: UserEntity): Result<UserEntity>
	suspend fun deleteUser(deletingUser: UserEntity): Result<Unit>

	// QR коды
	suspend fun getQrCodes(departmentId: String): Result<List<QREntity>>
	suspend fun postQrCode(departmentId: String, label: String): Result<QREntity>
	suspend fun downloadQrCode(qrCodeId: String): Result<ByteArray>
	suspend fun deleteQrCode(qrCodeId: String): Result<Unit>
}
