package studying.diplom.retailhub.presentation.di

import org.koin.dsl.module
import studying.diplom.retailhub.presentation.auth.AuthViewModel
import studying.diplom.retailhub.presentation.main.MainViewModel
import studying.diplom.retailhub.presentation.main.departments.department_list.DepartmentsListViewModel
import studying.diplom.retailhub.presentation.main.profile.ProfileViewModel
import studying.diplom.retailhub.presentation.main.requests.RequestsViewModel
import studying.diplom.retailhub.presentation.main.store.create_store.CreateStoreViewModel
import studying.diplom.retailhub.presentation.main.departments.department.DepartmentViewModel
import studying.diplom.retailhub.presentation.main.employees.employee.EmployeeViewModel
import studying.diplom.retailhub.presentation.main.employees.employees_list.EmployeesListViewModel
import studying.diplom.retailhub.presentation.main.store.my_store.MyStoreViewModel
import studying.diplom.retailhub.presentation.main.qr.create_qr.CreateQrViewModel
import studying.diplom.retailhub.presentation.main.qr.qr_list.QrListViewModel
import studying.diplom.retailhub.domain.models.user.UserModel
import studying.diplom.retailhub.presentation.notifications.NotificationsViewModel

val viewModelModule = module {
	single { RequestsViewModel(get(), get(), get(), get(), get(), get()) }
	factory { AuthViewModel(get(), get()) }
	factory { params -> MainViewModel(params.getOrNull(), get(), get(), get(), get()) }
	factory { ProfileViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
	factory { params -> CreateStoreViewModel(get(), get(), params.getOrNull()) }
	factory { MyStoreViewModel(get()) }
	factory { params -> 
        DepartmentViewModel(
            get(), get(), get(), get(), get(), get(), get(), get(), get(), 
            params.getOrNull()
        ) 
    }
	factory { DepartmentsListViewModel(get()) }
	factory { params -> 
        EmployeeViewModel(
            get(), get(), get(), get(), get(), get(), get(), get(),
            params.getOrNull<UserModel>()
        ) 
    }
	single { EmployeesListViewModel(get()) }
    factory { CreateQrViewModel(get(), get()) }
    factory { QrListViewModel(get(), get(), get(), get()) }
    factory { NotificationsViewModel(get(), get(), get()) }
}
