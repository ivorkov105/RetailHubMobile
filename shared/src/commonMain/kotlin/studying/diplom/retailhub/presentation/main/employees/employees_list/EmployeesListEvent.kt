package studying.diplom.retailhub.presentation.main.employees.employees_list

import studying.diplom.retailhub.domain.models.user.UserModel
import studying.diplom.retailhub.presentation.main.utils.UserRoles

sealed class EmployeesListEvent {
    object LoadList : EmployeesListEvent()
    data class OnEmployeeClick(val employee: UserModel) : EmployeesListEvent()
    data class OnFilterRole(val role: UserRoles?) : EmployeesListEvent()
}
