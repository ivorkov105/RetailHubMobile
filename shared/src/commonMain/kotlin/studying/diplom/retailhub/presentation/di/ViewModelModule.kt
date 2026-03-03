package studying.diplom.retailhub.presentation.di

import org.koin.dsl.module
import studying.diplom.retailhub.presentation.auth.AuthViewModel
import studying.diplom.retailhub.presentation.main.MainViewModel
import studying.diplom.retailhub.presentation.main.requests.RequestsViewModel

val viewModelModule = module {
    factory { RequestsViewModel(get()) }
    factory { AuthViewModel(get()) }
    factory { MainViewModel() }
}
