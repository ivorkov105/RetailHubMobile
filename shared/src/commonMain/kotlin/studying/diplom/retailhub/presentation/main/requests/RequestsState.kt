package studying.diplom.retailhub.presentation.main.requests

import studying.diplom.retailhub.domain.models.request.RequestModel

data class RequestsState(
    val requests: List<RequestModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val requestToAccept: RequestModel? = null,
    val requestToComplete: RequestModel? = null,
    val currentUserId: String = "",
    val currentUserFullName: String = "",
    val showStartShiftDialog: Boolean = false
)
