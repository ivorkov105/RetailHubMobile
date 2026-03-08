package studying.diplom.retailhub.presentation.auth

data class AuthState(
    val phoneNumber: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val role: String? = null
)
