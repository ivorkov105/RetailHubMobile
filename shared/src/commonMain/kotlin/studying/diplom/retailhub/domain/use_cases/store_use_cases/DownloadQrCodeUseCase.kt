package studying.diplom.retailhub.domain.use_cases.store_use_cases

import studying.diplom.retailhub.domain.repositories.StoreRepository

class DownloadQrCodeUseCase(private val repository: StoreRepository) {
    suspend operator fun invoke(qrCodeId: String): Result<ByteArray> {
        return repository.downloadQrCode(qrCodeId)
    }
}
