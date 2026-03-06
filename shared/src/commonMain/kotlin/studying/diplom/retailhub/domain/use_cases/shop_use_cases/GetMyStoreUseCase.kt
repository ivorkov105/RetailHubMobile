package studying.diplom.retailhub.domain.use_cases.shop_use_cases

import studying.diplom.retailhub.domain.models.shop.StoreModel
import studying.diplom.retailhub.domain.repositories.StoreRepository

class GetMyStoreUseCase(private val repository: StoreRepository) {
    suspend operator fun invoke(): Result<StoreModel> = repository.getMyStore()
}
