package studying.diplom.retailhub.presentation.auth

sealed class AuthEvent {
    data class OnPhoneNumberChange(val value: String) : AuthEvent()
    data class OnPasswordChange(val value: String) : AuthEvent()
    object OnLoginClick : AuthEvent()
    object OnErrorDismiss : AuthEvent()
}
