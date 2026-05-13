package studying.diplom.retailhub.presentation.main.requests

import studying.diplom.retailhub.domain.models.request.RequestModel
import studying.diplom.retailhub.domain.models.request.RequestStatus

sealed interface RequestsEvent {
    data class OnShowAcceptDialog(val request: RequestModel) : RequestsEvent
    data object OnDismissAcceptDialog : RequestsEvent
    data class OnShowCompleteDialog(val request: RequestModel) : RequestsEvent
    data object OnDismissCompleteDialog : RequestsEvent
    data class OnAcceptRequest(val requestId: String) : RequestsEvent
    data class OnCompleteRequest(val requestId: String) : RequestsEvent
    data object OnDismissErrorDialog : RequestsEvent
    data object OnRetryLoad : RequestsEvent
    
    data object OnToggleFilterDialog : RequestsEvent
    data class OnFilterStatusChange(val status: RequestStatus?) : RequestsEvent
    data class OnFilterDepartmentIdChange(val departmentId: String) : RequestsEvent
    data class OnFilterDateFromChange(val date: String?) : RequestsEvent
    data class OnFilterDateToChange(val date: String?) : RequestsEvent
    data object OnApplyFilters : RequestsEvent
    data object OnClearFilters : RequestsEvent
}
