package studying.diplom.retailhub.presentation.main.store.create_store

data class CreateStoreState(
    val name: String = "",
    val address: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)
