package studying.diplom.retailhub.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import studying.diplom.retailhub.data.di.dataModule
import studying.diplom.retailhub.data.di.networkModule
import studying.diplom.retailhub.domain.di.useCaseModule
import studying.diplom.retailhub.presentation.di.viewModelModule

fun initKoin(appDeclaration: KoinAppDeclaration = {} ) =
    startKoin {
        appDeclaration()
        modules(
            networkModule,
            dataModule,
            useCaseModule,
            viewModelModule,
            platformModule()
        )
    }

fun initKoin() = initKoin {}

expect fun platformModule(): Module
