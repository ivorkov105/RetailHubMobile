package studying.diplom.retailhub.presentation.main.departments.department

import studying.diplom.retailhub.domain.models.request.RequestModel
import studying.diplom.retailhub.domain.models.request.RequestStatus
import studying.diplom.retailhub.domain.models.shop.DepartmentModel

data class DepartmentState(
	val data : DepartmentModel? = null,
	val error : String? = null,
	val isLoading : Boolean = false,
    
    val requests: List<RequestModel> = emptyList(),
    val isRequestsLoading: Boolean = false,
    val showFilterDialog: Boolean = false,
    val filterStatus: RequestStatus? = null,
    val filterDateFrom: String? = null,
    val filterDateTo: String? = null,
    val currentUserId: String = "",
    
    val requestToAccept: RequestModel? = null,
    val requestToComplete: RequestModel? = null,
    val showStartShiftDialog: Boolean = false
)