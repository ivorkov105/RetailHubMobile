package studying.diplom.retailhub.data.di

import org.koin.dsl.module
import studying.diplom.retailhub.data.data_sources.DatabaseDriverFactory
import studying.diplom.retailhub.data.data_sources.LocalSource
import studying.diplom.retailhub.data.data_sources.RemoteSource
import studying.diplom.retailhub.data.data_sources.api.ApiClient
import studying.diplom.retailhub.data.data_sources.api.ApiClientImpl
import studying.diplom.retailhub.data.data_sources.api.StompService
import studying.diplom.retailhub.data.data_sources.api.WSService
import studying.diplom.retailhub.data.repositories.*
import studying.diplom.retailhub.database.RetailHubDatabase
import studying.diplom.retailhub.domain.repositories.*

val dataModule = module {
    single {
        val driver = get<DatabaseDriverFactory>().createDriver()
        RetailHubDatabase(driver = driver)
    }

    single<ApiClient> { ApiClientImpl(get()) }
    
    single { LocalSource(get()) }
    single { RemoteSource(get()) }

    single<WSService> { StompService(get(), get()) }

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<RequestRepository> { RequestRepositoryImpl(get(), get(), get()) }
    single<StoreRepository> { StoreRepositoryImpl(get(), get()) }
	single<UserRepository> { UserRepositoryImpl(get(), get()) }
	single<ShiftRepository> { ShiftRepositoryImpl(get()) }
    single<NotificationRepository> { NotificationRepositoryImpl(get(), get()) }
    single<AnalyticsRepository> { AnalyticsRepositoryImpl(get()) }
    single<DeviceRepository> { DeviceRepositoryImpl(get()) }
}
