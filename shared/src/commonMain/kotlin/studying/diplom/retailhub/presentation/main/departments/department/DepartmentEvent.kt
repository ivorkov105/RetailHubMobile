package studying.diplom.retailhub.presentation.main.departments.department

import studying.diplom.retailhub.domain.models.request.RequestModel
import studying.diplom.retailhub.domain.models.request.RequestStatus
import studying.diplom.retailhub.domain.models.shop.DepartmentModel

sealed class DepartmentEvent {

	data class OnNameChange(val value: String) : DepartmentEvent()
	data class OnDescriptionChange(val value: String) : DepartmentEvent()
	data class OnStoreChange(val value: DepartmentModel) : DepartmentEvent()

	object OnLoadDepartment : DepartmentEvent()
	object OnSaveClick : DepartmentEvent()
	object OnUpdateClick : DepartmentEvent()
	object OnDeleteClick : DepartmentEvent()
	object OnBackClick : DepartmentEvent()
    
    // Requests
    object OnToggleFilterDialog : DepartmentEvent()
    data class OnFilterStatusChange(val status: RequestStatus?) : DepartmentEvent()
    data class OnFilterDateFromChange(val date: String?) : DepartmentEvent()
    data class OnFilterDateToChange(val date: String?) : DepartmentEvent()
    object OnApplyFilters : DepartmentEvent()
    object OnClearFilters : DepartmentEvent()
    
    data class OnAcceptRequest(val requestId: String) : DepartmentEvent()
    data class OnCompleteRequest(val requestId: String) : DepartmentEvent()
    
    data class OnShowAcceptDialog(val request: RequestModel) : DepartmentEvent()
    data object OnDismissAcceptDialog : DepartmentEvent()
    data class OnShowCompleteDialog(val request: RequestModel) : DepartmentEvent()
    data object OnDismissCompleteDialog : DepartmentEvent()
    
    data object OnConfirmStartShift : DepartmentEvent()
    data object OnDismissStartShiftDialog : DepartmentEvent()
    data object OnDismissErrorDialog : DepartmentEvent()
}