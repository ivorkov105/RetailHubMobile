package studying.diplom.retailhub.domain.di

import org.koin.dsl.module
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.AddRequestsUseCase
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.GetRequestsUseCase

val useCaseModule = module {
    factory { GetRequestsUseCase(get()) }
    factory { AddRequestsUseCase(get()) }
}
