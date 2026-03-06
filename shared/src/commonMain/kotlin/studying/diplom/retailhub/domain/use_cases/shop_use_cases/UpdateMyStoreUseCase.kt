package studying.diplom.retailhub.domain.use_cases.shop_use_cases

import studying.diplom.retailhub.domain.models.shop.StoreModel
import studying.diplom.retailhub.domain.repositories.StoreRepository

class UpdateMyStoreUseCase(private val repository: StoreRepository) {
    suspend operator fun invoke(store: StoreModel): Result<StoreModel> = repository.updateMyStore(store)
}
