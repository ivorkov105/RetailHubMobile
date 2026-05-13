package studying.diplom.retailhub.presentation.main.profile

import studying.diplom.retailhub.domain.models.analytics.Period

sealed class ProfileEvent {
    object LoadProfile : ProfileEvent()
    object Logout : ProfileEvent()
    object OnMyStoreClick : ProfileEvent()
    object OnCreateStoreClick : ProfileEvent()
	object OnUpdateStoreClick : ProfileEvent()
	object OnCreateDepartmentClick : ProfileEvent()
	object OnCreateEmployeeClick : ProfileEvent()
	object OnQrClick : ProfileEvent()
    object OnQrListClick : ProfileEvent()
    object OnStartShiftClick : ProfileEvent()
    object OnEndShiftClick : ProfileEvent()
    data class OnDateFromChange(val date: String?) : ProfileEvent()
    data class OnDateToChange(val date: String?) : ProfileEvent()
    object LoadShifts : ProfileEvent()
    data class OnPeriodChange(val period: Period) : ProfileEvent()
}
