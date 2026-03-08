package studying.diplom.retailhub.presentation.main.employees.employee

import studying.diplom.retailhub.presentation.main.utils.UserRoles

sealed class EmployeeEvent {
    object OnLoadEmployee : EmployeeEvent()
    object OnSaveClick : EmployeeEvent()
    object OnUpdateClick : EmployeeEvent()
    object OnDeleteClick : EmployeeEvent()
    object OnBackClick : EmployeeEvent()
    data class OnNameChange(val value: String) : EmployeeEvent()
    data class OnSurnameChange(val value: String) : EmployeeEvent()
    data class OnPhoneNumberChange(val value: String) : EmployeeEvent()
    data class OnPasswordChange(val value: String) : EmployeeEvent()
    data class OnRoleChange(val value: UserRoles) : EmployeeEvent()
    data class OnDepartmentToggle(val departmentId: String) : EmployeeEvent()
}
