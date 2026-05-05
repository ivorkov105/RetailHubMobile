package studying.diplom.retailhub.presentation.main.profile

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
    object OnNotificationsClick : ProfileEvent()
}
