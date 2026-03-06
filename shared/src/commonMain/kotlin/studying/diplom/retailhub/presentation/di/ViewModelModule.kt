package studying.diplom.retailhub.presentation.di

import org.koin.dsl.module
import org.koin.core.parameter.parametersOf
import studying.diplom.retailhub.presentation.auth.AuthViewModel
import studying.diplom.retailhub.presentation.main.MainViewModel
import studying.diplom.retailhub.presentation.main.profile.ProfileViewModel
import studying.diplom.retailhub.presentation.main.requests.RequestsViewModel
import studying.diplom.retailhub.presentation.main.store.create_store.CreateStoreViewModel
import studying.diplom.retailhub.presentation.main.store.my_store.MyStoreViewModel

val viewModelModule = module {
    factory { RequestsViewModel(get()) }
    factory { AuthViewModel(get()) }
    factory { MainViewModel() }
    factory { ProfileViewModel(get(), get(), get()) }
    factory { params -> CreateStoreViewModel(get(), get(), params.getOrNull()) }
    factory { MyStoreViewModel(get()) }
}
