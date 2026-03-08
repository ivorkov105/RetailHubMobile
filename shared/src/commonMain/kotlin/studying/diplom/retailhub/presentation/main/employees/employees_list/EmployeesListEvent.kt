package studying.diplom.retailhub.presentation.main.employees.employees_list

import studying.diplom.retailhub.domain.models.user.UserModel

sealed class EmployeesListEvent {
    object LoadList : EmployeesListEvent()
    data class OnEmployeeClick(val employee: UserModel) : EmployeesListEvent()
}
