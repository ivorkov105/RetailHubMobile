package studying.diplom.retailhub.presentation.main.departments.department

import studying.diplom.retailhub.domain.models.shop.DepartmentModel

data class DepartmentState(
	val data : DepartmentModel? = null,
	val error : String? = null,
	val isLoading : Boolean = false
)