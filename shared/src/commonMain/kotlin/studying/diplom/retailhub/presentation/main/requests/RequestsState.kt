package studying.diplom.retailhub.presentation.main.requests

import studying.diplom.retailhub.domain.models.request.RequestModel

data class RequestsState(
    val requests: List<RequestModel> = emptyList(),
    val isLoading: Boolean = false,
    val isPaginationLoading: Boolean = false,
    val error: String? = null,
    val statusFilter: String = "",
    val departmentFilter: String = "",
    val dateFromFilter: String = "",
    val dateToFilter: String = "",
    val currentPage: Int = 0,
    val pageSize: Int = 20,
    val isLastPage: Boolean = false,
    val requestToAccept: RequestModel? = null,
    val requestToComplete: RequestModel? = null,
    val currentUserFullName: String = "",
    val showStartShiftDialog: Boolean = false
)
