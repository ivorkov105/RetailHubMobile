package studying.diplom.retailhub.domain.use_cases.store_use_cases

import studying.diplom.retailhub.domain.models.shop.StoreModel
import studying.diplom.retailhub.domain.repositories.StoreRepository

class AddStoreUseCase(private val repository: StoreRepository) {
    suspend operator fun invoke(store: StoreModel): Result<Unit> = repository.addStore(store)
}
