package studying.diplom.retailhub.presentation.main.shop

data class StoreManagementState(
    val name: String = "",
    val address: String = "",
    val description: String = "",
    val isReadOnly: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)
