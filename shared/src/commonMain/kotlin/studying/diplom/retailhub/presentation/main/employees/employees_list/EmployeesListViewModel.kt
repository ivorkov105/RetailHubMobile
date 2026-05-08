package studying.diplom.retailhub.presentation.main.employees.employees_list

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
import studying.diplom.retailhub.domain.models.user.UserModel
import studying.diplom.retailhub.domain.use_cases.user_use_cases.GetStoreUsersUseCase

sealed class EmployeesListNavigationEvent {
    data class NavigateToEmployee(val employee: UserModel) : EmployeesListNavigationEvent()
}

class EmployeesListViewModel(
    private val getStoreUsersUseCase: GetStoreUsersUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(EmployeesListState())
    val state: StateFlow<EmployeesListState> = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<EmployeesListNavigationEvent>()
    val navigationEvents: SharedFlow<EmployeesListNavigationEvent> = _navigationEvents.asSharedFlow()

    fun onEvent(event: EmployeesListEvent) {
        when (event) {
            is EmployeesListEvent.LoadList -> loadEmployees()
            is EmployeesListEvent.OnEmployeeClick -> {
                screenModelScope.launch {
                    _navigationEvents.emit(EmployeesListNavigationEvent.NavigateToEmployee(event.employee))
                }
            }
            is EmployeesListEvent.OnFilterRole -> {
                _state.update { it.copy(selectedRole = event.role) }
            }
        }
    }

    private fun loadEmployees() {
        screenModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getStoreUsersUseCase().onSuccess { result ->
                _state.update { it.copy(employees = result, isLoading = false) }
            }.onFailure { throwable ->
                _state.update { it.copy(
                    isLoading = false,
                    error = throwable.message ?: "Ошибка при загрузке сотрудников"
                ) }
            }
        }
    }
}
