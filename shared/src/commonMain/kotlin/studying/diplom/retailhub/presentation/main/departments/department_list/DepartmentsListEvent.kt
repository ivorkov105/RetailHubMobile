package studying.diplom.retailhub.presentation.main.departments.department_list

import studying.diplom.retailhub.domain.models.shop.DepartmentModel

sealed class DepartmentsListEvent {
    object LoadList : DepartmentsListEvent()
    data class OnDepartmentClick(val department: DepartmentModel) : DepartmentsListEvent()
    object OnBackClick : DepartmentsListEvent()
}
