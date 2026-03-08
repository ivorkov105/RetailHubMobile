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

val viewModelModule = module {
	factory { RequestsViewModel(get()) }
	factory { AuthViewModel(get()) }
	factory { MainViewModel() }
	factory { ProfileViewModel(get(), get(), get()) }
	factory { params -> CreateStoreViewModel(get(), get(), params.getOrNull()) }
	factory { MyStoreViewModel(get()) }
	factory { params -> DepartmentViewModel(get(), get(), get(), get(), params.getOrNull()) }
	factory { DepartmentsListViewModel(get()) }
	factory { params -> EmployeeViewModel(get(),get(),get(), get(),get(),params.getOrNull()) }
	factory { EmployeesListViewModel(get()) }
}
