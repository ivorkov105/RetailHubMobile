package studying.diplom.retailhub.presentation.main.store.create_store

sealed class CreateStoreEvent {
    data class OnNameChange(val value: String) : CreateStoreEvent()
    data class OnAddressChange(val value: String) : CreateStoreEvent()
    object OnSaveClick : CreateStoreEvent()
    object OnErrorDismiss : CreateStoreEvent()
    object OnBackClick : CreateStoreEvent()
}
