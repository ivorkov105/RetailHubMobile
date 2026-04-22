package studying.diplom.retailhub.domain.use_cases.store_use_cases

import studying.diplom.retailhub.domain.models.qr_code.QrCodeModel
import studying.diplom.retailhub.domain.repositories.StoreRepository

class PostQrCodeUseCase(private val repository: StoreRepository) {
    suspend operator fun invoke(departmentId: String, label: String): Result<QrCodeModel> {
        return repository.postQrCode(departmentId, label)
    }
}
