package studying.diplom.retailhub.presentation.main.employees.employees_list

import studying.diplom.retailhub.domain.models.user.UserModel
import studying.diplom.retailhub.presentation.main.utils.UserRoles

data class EmployeesListState(
    val employees: List<UserModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedRole: UserRoles? = null
)
