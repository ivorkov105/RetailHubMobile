package studying.diplom.retailhub.presentation.main.requests

import studying.diplom.retailhub.domain.models.request.RequestModel
import studying.diplom.retailhub.domain.models.request.RequestStatus

data class RequestsState(
    val requests: List<RequestModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val requestToAccept: RequestModel? = null,
    val requestToComplete: RequestModel? = null,
    val currentUserId: String = "",
    val currentUserFullName: String = "",
    val currentUserRole: String = "",
    val currentUserDepartmentIds: List<String> = emptyList(),
    
    val showFilterDialog: Boolean = false,
    val filterStatus: RequestStatus? = null,
    val filterDepartmentId: String = "",
    val filterDateFrom: String? = null,
    val filterDateTo: String? = null
)
