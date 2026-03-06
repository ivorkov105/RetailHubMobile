package studying.diplom.retailhub.presentation.main.store.my_store

import studying.diplom.retailhub.domain.models.shop.StoreModel

data class MyStoreState(
    val store: StoreModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
