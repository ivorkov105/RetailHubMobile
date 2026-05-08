package studying.diplom.retailhub.presentation.main.employees.employee

import studying.diplom.retailhub.domain.models.shop.DepartmentModel
import studying.diplom.retailhub.domain.models.user.UserModel
import studying.diplom.retailhub.domain.models.analytics.ConsultantDetailStatsModel

data class EmployeeState(
    val data: UserModel? = null,
    val availableDepartments: List<DepartmentModel> = emptyList(),
    val consultantStats: ConsultantDetailStatsModel? = null,
    val isManager: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val analyticsError: String? = null,
    val isSuccess: Boolean = false
)
