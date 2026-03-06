package studying.diplom.retailhub.presentation.main.shop

sealed class StoreManagementEvent {
    data class OnNameChange(val value: String) : StoreManagementEvent()
    data class OnAddressChange(val value: String) : StoreManagementEvent()
    data class OnDescriptionChange(val value: String) : StoreManagementEvent()
    object OnSaveClick : StoreManagementEvent()
    object OnErrorDismiss : StoreManagementEvent()
    object OnBackClick : StoreManagementEvent()
    object LoadStore : StoreManagementEvent()
}
