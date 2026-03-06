package studying.diplom.retailhub.presentation.main.store.my_store

sealed class MyStoreEvent {
    object LoadStore : MyStoreEvent()
    object OnBackClick : MyStoreEvent()
}
