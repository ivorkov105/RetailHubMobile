package studying.diplom.retailhub.domain.use_cases.store_use_cases

import studying.diplom.retailhub.domain.models.qr_code.QrCodeModel
import studying.diplom.retailhub.domain.repositories.StoreRepository

class GetQrCodesUseCase(private val repository: StoreRepository) {
    suspend operator fun invoke(departmentId: String = ""): Result<List<QrCodeModel>> {
        return repository.getQrCodes(departmentId)
    }
}
