package studying.diplom.retailhub.domain.di

import org.koin.dsl.module
import studying.diplom.retailhub.domain.use_cases.analytics_use_cases.GetAnalyticsDashboardUseCase
import studying.diplom.retailhub.domain.use_cases.analytics_use_cases.GetConsultantDetailStatsUseCase
import studying.diplom.retailhub.domain.use_cases.analytics_use_cases.GetConsultantsStatsUseCase
import studying.diplom.retailhub.domain.use_cases.auth_use_cases.*
import studying.diplom.retailhub.domain.use_cases.notifications_use_cases.*
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.AddRequestsUseCase
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.AssignRequestUseCase
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.CompleteRequestUseCase
import studying.diplom.retailhub.domain.use_cases.shift_use_cases.EndShiftUseCase
import studying.diplom.retailhub.domain.use_cases.shift_use_cases.StartShiftUseCase
import studying.diplom.retailhub.domain.use_cases.store_use_cases.*
import studying.diplom.retailhub.domain.use_cases.user_use_cases.*

val useCaseModule = module {
	factory { AddRequestsUseCase(get()) }
	factory { AssignRequestUseCase(get()) }
	factory { CompleteRequestUseCase(get()) }

	factory { AddStoreUseCase(get()) }
	factory { GetMyStoreUseCase(get()) }
	factory { UpdateMyStoreUseCase(get()) }

	factory { AddDepartmentUseCase(get()) }
	factory { GetDepartmentsUseCase(get()) }
	factory { GetDepartmentUseCase(get()) }
	factory { UpdateDepartmentUseCase(get()) }
	factory { DeleteDepartmentUseCase(get()) }
    factory { PostQrCodeUseCase(get()) }
    factory { GetQrCodesUseCase(get()) }
    factory { DeleteQrCodeUseCase(get()) }
    factory { DownloadQrCodeUseCase(get()) }

	factory { LoginUseCase(get()) }
	factory { GetProfileUseCase(get()) }
	factory { LogoutUseCase(get(), get()) }
	factory { IsAuthorizedUseCase(get()) }
	factory { GetSavedRoleUseCase(get()) }

	factory { GetStoreUsersUseCase(get()) }
	factory { GetUserUseCase(get()) }
	factory { AddUserUseCase(get()) }
	factory { UpdateUserUseCase(get()) }
	factory { UpdateUserDepartmentsUseCase(get()) }
	factory { DeleteUserUseCase(get()) }

	factory { StartShiftUseCase(get()) }
	factory { EndShiftUseCase(get()) }

    factory { GetNotificationsUseCase(get()) }
    factory { RefreshNotificationsUseCase(get()) }
    factory { MarkNotificationReadUseCase(get()) }
    factory { RegisterDeviceUseCase(get()) }
    factory { UnregisterDeviceUseCase(get()) }
    factory { SaveNotificationUseCase(get()) }

    factory { GetAnalyticsDashboardUseCase(get()) }
    factory { GetConsultantsStatsUseCase(get()) }
    factory { GetConsultantDetailStatsUseCase(get()) }
}
