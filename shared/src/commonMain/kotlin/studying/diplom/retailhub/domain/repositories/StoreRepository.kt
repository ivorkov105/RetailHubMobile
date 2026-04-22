package studying.diplom.retailhub.domain.repositories

import studying.diplom.retailhub.domain.models.shop.DepartmentModel
import studying.diplom.retailhub.domain.models.shop.StoreModel
import studying.diplom.retailhub.domain.models.qr_code.QrCodeModel

interface StoreRepository {
    suspend fun addStore(store: StoreModel): Result<Unit>
    suspend fun getMyStore(): Result<StoreModel>
    suspend fun updateMyStore(store: StoreModel): Result<StoreModel>
    
    suspend fun addDepartment(department: DepartmentModel): Result<Unit>
    suspend fun getDepartments(): Result<List<DepartmentModel>>
	suspend fun getDepartment(id: String): Result<DepartmentModel>
	suspend fun updateDepartment(department: DepartmentModel): Result<DepartmentModel>
    suspend fun deleteDepartment(department: DepartmentModel): Result<Unit>

    suspend fun postQrCode(departmentId: String, label: String): Result<QrCodeModel>
    suspend fun getQrCodes(departmentId: String): Result<List<QrCodeModel>>
    suspend fun deleteQrCode(qrCodeId: String): Result<Unit>
    suspend fun downloadQrCode(qrCodeId: String): Result<ByteArray>
}
