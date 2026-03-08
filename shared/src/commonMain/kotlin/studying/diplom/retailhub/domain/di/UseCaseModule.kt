package studying.diplom.retailhub.domain.di

import org.koin.dsl.module
import studying.diplom.retailhub.domain.use_cases.auth_use_cases.GetProfileUseCase
import studying.diplom.retailhub.domain.use_cases.auth_use_cases.LogoutUseCase
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.AddRequestsUseCase
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.GetRequestsUseCase
import studying.diplom.retailhub.domain.use_cases.store_use_cases.*
import studying.diplom.retailhub.domain.use_cases.user_use_cases.*

val useCaseModule = module {
    factory { GetRequestsUseCase(get()) }
    factory { AddRequestsUseCase(get()) }
    
    factory { AddStoreUseCase(get()) }
    factory { GetMyStoreUseCase(get()) }
    factory { UpdateMyStoreUseCase(get()) }

    factory { AddDepartmentUseCase(get()) }
    factory { GetDepartmentsUseCase(get()) }
	factory { GetDepartmentUseCase(get()) }
    factory { UpdateDepartmentUseCase(get()) }
    factory { DeleteDepartmentUseCase(get()) }

    factory { GetProfileUseCase(get()) }
    factory { LogoutUseCase(get()) }

	factory { GetStoreUsersUseCase(get()) }
	factory { GetUserUseCase(get()) }
	factory { AddUserUseCase(get()) }
	factory { UpdateUserUseCase(get()) }
	factory { DeleteUserUseCase(get()) }
}
