package studying.diplom.retailhub.presentation.main.requests

import studying.diplom.retailhub.domain.models.request.RequestModel

sealed interface RequestsEvent {
    data class OnShowAcceptDialog(val request: RequestModel) : RequestsEvent
    data object OnDismissAcceptDialog : RequestsEvent
    data class OnShowCompleteDialog(val request: RequestModel) : RequestsEvent
    data object OnDismissCompleteDialog : RequestsEvent
    data class OnAcceptRequest(val requestId: String) : RequestsEvent
    data class OnCompleteRequest(val requestId: String) : RequestsEvent
    data object OnDismissErrorDialog : RequestsEvent
    data object OnConfirmStartShift : RequestsEvent
    data object OnDismissStartShiftDialog : RequestsEvent
}
