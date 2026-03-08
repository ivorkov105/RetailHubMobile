package studying.diplom.retailhub.presentation.main.departments.department

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
}