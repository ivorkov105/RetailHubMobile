package studying.diplom.retailhub.presentation.main.employees.employee

import studying.diplom.retailhub.domain.models.shop.DepartmentModel
import studying.diplom.retailhub.domain.models.user.UserModel

data class EmployeeState(
    val data: UserModel? = null,
    val availableDepartments: List<DepartmentModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)
