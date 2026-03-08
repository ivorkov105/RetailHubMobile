package studying.diplom.retailhub.presentation.main.departments.department_list

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
import studying.diplom.retailhub.domain.use_cases.store_use_cases.GetDepartmentsUseCase

sealed class DepartmentsListNavigationEvent {
    data class NavigateToDepartment(val department: DepartmentModel) : DepartmentsListNavigationEvent()
}

class DepartmentsListViewModel(
    private val getDepartmentsUseCase: GetDepartmentsUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(DepartmentsListState())
    val state: StateFlow<DepartmentsListState> = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<DepartmentsListNavigationEvent>()
    val navigationEvents: SharedFlow<DepartmentsListNavigationEvent> = _navigationEvents.asSharedFlow()

    fun onEvent(event: DepartmentsListEvent) {
        when (event) {
            is DepartmentsListEvent.LoadList -> loadDepartments()
            is DepartmentsListEvent.OnDepartmentClick -> {
                screenModelScope.launch {
                    _navigationEvents.emit(DepartmentsListNavigationEvent.NavigateToDepartment(event.department))
                }
            }
            is DepartmentsListEvent.OnBackClick -> {
                // В табе "Назад" обычно не требуется, но можно добавить логику
            }
        }
    }

    private fun loadDepartments() {
        screenModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            getDepartmentsUseCase().onSuccess { result ->
                _state.update { it.copy(departments = result, isLoading = false) }
            }.onFailure { throwable ->
                _state.update { it.copy(
                    isLoading = false,
                    error = throwable.message ?: "Ошибка при загрузке отделов"
                ) }
            }
        }
    }
}
