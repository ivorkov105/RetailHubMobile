package studying.diplom.retailhub.domain.di

import org.koin.dsl.module
import studying.diplom.retailhub.data.data_sources.LocalSource
import studying.diplom.retailhub.data.data_sources.RemoteSource
import studying.diplom.retailhub.data.repositories.RequestRepositoryImpl
import studying.diplom.retailhub.domain.repositories.RequestRepository

val repositoryModule = module {
    single { LocalSource(get()) }
    single { RemoteSource(get()) }

    single<RequestRepository> { 
        RequestRepositoryImpl(
            remoteSource = get(), 
            localSource = get()
        ) 
    }
}
