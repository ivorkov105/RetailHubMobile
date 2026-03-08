package studying.diplom.retailhub.presentation.main.departments.department

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import studying.diplom.retailhub.domain.models.shop.DepartmentModel
import studying.diplom.retailhub.domain.use_cases.store_use_cases.AddDepartmentUseCase
import studying.diplom.retailhub.domain.use_cases.store_use_cases.DeleteDepartmentUseCase
import studying.diplom.retailhub.domain.use_cases.store_use_cases.GetDepartmentUseCase
import studying.diplom.retailhub.domain.use_cases.store_use_cases.UpdateDepartmentUseCase

sealed class DepartmentNavigationEvent {
	object NavigateBack : DepartmentNavigationEvent()
}

class DepartmentViewModel(
	private val addDepartmentUseCase: AddDepartmentUseCase,
	private val getDepartmentUseCase: GetDepartmentUseCase,
	private val updateDepartmentUseCase: UpdateDepartmentUseCase,
	private val deleteDepartmentUseCase: DeleteDepartmentUseCase,
	private val initialDepartment: DepartmentModel? = null
) : ScreenModel {

	private val _state = MutableStateFlow(DepartmentState(data = initialDepartment ?: DepartmentModel()))
	val state: StateFlow<DepartmentState> = _state.asStateFlow()

	private val _navigationEvents = MutableSharedFlow<DepartmentNavigationEvent>()
	val navigationEvents: SharedFlow<DepartmentNavigationEvent> = _navigationEvents.asSharedFlow()

	fun onEvent(event: DepartmentEvent) {
		when (event) {
			is DepartmentEvent.OnLoadDepartment -> loadDepartment()
			is DepartmentEvent.OnSaveClick -> saveDepartment()
			is DepartmentEvent.OnUpdateClick -> updateDepartment()
			is DepartmentEvent.OnDeleteClick -> deleteDepartment()
			is DepartmentEvent.OnBackClick -> {
				screenModelScope.launch {
					_navigationEvents.emit(DepartmentNavigationEvent.NavigateBack)
				}
			}
			is DepartmentEvent.OnNameChange -> {
				_state.update { it.copy(data = it.data?.copy(name = event.value)) }
			}
			is DepartmentEvent.OnDescriptionChange -> {
				_state.update { it.copy(data = it.data?.copy(description = event.value)) }
			}
			is DepartmentEvent.OnStoreChange -> {
				_state.update { it.copy(data = it.data?.copy(storeId = event.value.id)) }
			}
		}
	}

	private fun loadDepartment() {
		screenModelScope.launch {
			_state.update { it.copy(isLoading = true, error = null) }
			getDepartmentUseCase(initialDepartment?.id ?: return@launch)
				.onSuccess { updatedData ->
					_state.update {
						it.copy(
							data = updatedData,
							isLoading = false
						)
					}
				}
				.onFailure { throwable ->
					_state.update { it.copy(isLoading = false, error = throwable.message) }
				}
		}
	}

	private fun saveDepartment() {
		val currentState = _state.value
		val departmentData = currentState.data ?: return

		if (departmentData.name.isBlank()) {
			_state.update { it.copy(error = "Введите название отдела") }
			return
		}

		screenModelScope.launch {
			_state.update { it.copy(isLoading = true, error = null) }
			addDepartmentUseCase(departmentData).onSuccess {
				_state.update { it.copy(isLoading = false) }
				_navigationEvents.emit(DepartmentNavigationEvent.NavigateBack)
			}.onFailure { throwable ->
				_state.update { it.copy(isLoading = false, error = throwable.message) }
			}
		}
	}

	private fun updateDepartment() {
		val currentState = _state.value
		val departmentData = currentState.data ?: return

		screenModelScope.launch {
			_state.update { it.copy(isLoading = true, error = null) }
			updateDepartmentUseCase(departmentData).onSuccess {
				_state.update { it.copy(isLoading = false) }
				_navigationEvents.emit(DepartmentNavigationEvent.NavigateBack)
			}.onFailure { throwable ->
				_state.update { it.copy(isLoading = false, error = throwable.message) }
			}
		}
	}

	private fun deleteDepartment() {
		val departmentData = _state.value.data ?: return
		screenModelScope.launch {
			_state.update { it.copy(isLoading = true, error = null) }
			deleteDepartmentUseCase(departmentData).onSuccess {
				_state.update { it.copy(isLoading = false) }
				_navigationEvents.emit(DepartmentNavigationEvent.NavigateBack)
			}.onFailure { throwable ->
				_state.update { it.copy(isLoading = false, error = throwable.message) }
			}
		}
	}
}
