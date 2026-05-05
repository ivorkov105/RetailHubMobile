package studying.diplom.retailhub.presentation.main.employees.employee

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import studying.diplom.retailhub.data.data_sources.api.ApiException
import studying.diplom.retailhub.domain.models.user.UserModel
import studying.diplom.retailhub.domain.use_cases.store_use_cases.GetDepartmentsUseCase
import studying.diplom.retailhub.domain.use_cases.user_use_cases.AddUserUseCase
import studying.diplom.retailhub.domain.use_cases.user_use_cases.DeleteUserUseCase
import studying.diplom.retailhub.domain.use_cases.user_use_cases.GetUserUseCase
import studying.diplom.retailhub.domain.use_cases.user_use_cases.UpdateUserDepartmentsUseCase
import studying.diplom.retailhub.domain.use_cases.user_use_cases.UpdateUserUseCase
import studying.diplom.retailhub.presentation.main.utils.UserRoles

sealed class EmployeeNavigationEvent {
	object NavigateBack : EmployeeNavigationEvent()
	object NavigateToAuth : EmployeeNavigationEvent()
}

class EmployeeViewModel(
	private val getUserUseCase: GetUserUseCase,
	private val addUserUseCase: AddUserUseCase,
	private val updateUserUseCase: UpdateUserUseCase,
	private val deleteUserUseCase: DeleteUserUseCase,
	private val getDepartmentsUseCase: GetDepartmentsUseCase,
	private val updateUserDepartmentsUseCase: UpdateUserDepartmentsUseCase,
	private val initialEmployee: UserModel? = null
) : ScreenModel {

	private val _state = MutableStateFlow(
		EmployeeState(
			data = initialEmployee ?: UserModel(
				id = "",
				storeId = "",
				phoneNumber = "",
				firstName = "",
				lastName = "",
				role = UserRoles.CONSULTANT.name,
				currentStatus = "ACTIVE",
				departments = emptyList(),
				password = "",
				createdAt = ""
			)
		)
	)
	val state: StateFlow<EmployeeState> = _state.asStateFlow()

	private val _navigationEvents = MutableSharedFlow<EmployeeNavigationEvent>()
	val navigationEvents: SharedFlow<EmployeeNavigationEvent> = _navigationEvents.asSharedFlow()

	init {
		loadDepartments()
	}

	private fun handleError(throwable: Throwable) {
		if (throwable is ApiException && throwable.statusCode == HttpStatusCode.Unauthorized) {
			screenModelScope.launch {
				_navigationEvents.emit(EmployeeNavigationEvent.NavigateToAuth)
			}
		} else {
			_state.update { it.copy(isLoading = false, error = throwable.message ?: "Произошла ошибка") }
		}
	}

	fun onEvent(event: EmployeeEvent) {
		when (event) {
			is EmployeeEvent.OnLoadEmployee      -> loadEmployee()

			is EmployeeEvent.OnNameChange        -> {
				_state.update { it.copy(data = it.data?.copy(firstName = event.value)) }
			}

			is EmployeeEvent.OnSurnameChange     -> {
				_state.update { it.copy(data = it.data?.copy(lastName = event.value)) }
			}

			is EmployeeEvent.OnPhoneNumberChange -> {
				_state.update { it.copy(data = it.data?.copy(phoneNumber = event.value)) }
			}

			is EmployeeEvent.OnPasswordChange    -> {
				_state.update { it.copy(data = it.data?.copy(password = event.value)) }
			}

			is EmployeeEvent.OnRoleChange        -> {
				_state.update { it.copy(data = it.data?.copy(role = event.value.name)) }
			}

			is EmployeeEvent.OnDepartmentToggle  -> toggleDepartment(event.departmentId)
			is EmployeeEvent.OnSaveClick         -> saveEmployee()
			is EmployeeEvent.OnUpdateClick       -> updateEmployee()
			is EmployeeEvent.OnDeleteClick       -> deleteEmployee()

			is EmployeeEvent.OnBackClick         -> {
				screenModelScope.launch {
					_navigationEvents.emit(EmployeeNavigationEvent.NavigateBack)
				}
			}
		}
	}

	private fun loadDepartments() {
		screenModelScope.launch {
			getDepartmentsUseCase().onSuccess { departments ->
				_state.update { it.copy(availableDepartments = departments) }
			}.onFailure { handleError(it) }
		}
	}

	private fun loadEmployee() {
		val id = initialEmployee?.id
		if (id.isNullOrEmpty()) return

		screenModelScope.launch {
			_state.update { it.copy(isLoading = true, error = null) }
			getUserUseCase(id).onSuccess { user ->
				_state.update { it.copy(data = user, isLoading = false) }
			}.onFailure { handleError(it) }
		}
	}

	private fun toggleDepartment(deptId: String) {
		val currentData = _state.value.data ?: return
		val currentDepts = currentData.departments
		val isSelected = currentDepts.any { it.id == deptId }

		val newDepts = if (isSelected) {
			currentDepts.filter { it.id != deptId }
		} else {
			val deptToAdd = _state.value.availableDepartments.find { it.id == deptId }
			if (deptToAdd != null) currentDepts + deptToAdd else currentDepts
		}

		_state.update { it.copy(data = currentData.copy(departments = newDepts)) }
	}

	private fun saveEmployee() {
		val userData = _state.value.data ?: return
		screenModelScope.launch {
			_state.update { it.copy(isLoading = true, error = null) }
			addUserUseCase(userData).onSuccess {
				_state.update { it.copy(isLoading = false) }
				_navigationEvents.emit(EmployeeNavigationEvent.NavigateBack)
			}.onFailure { handleError(it) }
		}
	}

	private fun updateEmployee() {
		val userData = _state.value.data ?: return
		screenModelScope.launch {
			_state.update { it.copy(isLoading = true, error = null) }

			updateUserUseCase(userData).onSuccess {
				updateUserDepartmentsUseCase(userData.id, userData.departments.map { it.id }).onSuccess {
					_state.update { it.copy(isLoading = false) }
					_navigationEvents.emit(EmployeeNavigationEvent.NavigateBack)
				}.onFailure { handleError(it) }
			}.onFailure { handleError(it) }
		}
	}

	private fun deleteEmployee() {
		val userData = _state.value.data ?: return
		screenModelScope.launch {
			_state.update { it.copy(isLoading = true, error = null) }
			deleteUserUseCase(userData).onSuccess {
				_state.update { it.copy(isLoading = false) }
				_navigationEvents.emit(EmployeeNavigationEvent.NavigateBack)
			}.onFailure { handleError(it) }
		}
	}
}
