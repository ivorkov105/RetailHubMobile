package studying.diplom.retailhub.presentation.main.departments.department_list

import studying.diplom.retailhub.domain.models.shop.DepartmentModel

data class DepartmentsListState(
    val departments: List<DepartmentModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
