package studying.diplom.retailhub.domain.use_cases.store_use_cases

import studying.diplom.retailhub.domain.repositories.StoreRepository

class DeleteQrCodeUseCase(private val repository: StoreRepository) {
    suspend operator fun invoke(qrCodeId: String): Result<Unit> {
        return repository.deleteQrCode(qrCodeId)
    }
}
