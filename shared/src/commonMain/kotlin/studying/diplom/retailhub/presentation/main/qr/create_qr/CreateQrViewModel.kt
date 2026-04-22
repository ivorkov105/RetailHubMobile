package studying.diplom.retailhub.presentation.main.qr.create_qr

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
import studying.diplom.retailhub.domain.use_cases.store_use_cases.GetDepartmentsUseCase
import studying.diplom.retailhub.domain.use_cases.store_use_cases.PostQrCodeUseCase

sealed class CreateQrNavigationEvent {
    object NavigateBack : CreateQrNavigationEvent()
}

class CreateQrViewModel(
    private val getDepartmentsUseCase: GetDepartmentsUseCase,
    private val postQrCodeUseCase: PostQrCodeUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(CreateQrState())
    val state: StateFlow<CreateQrState> = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<CreateQrNavigationEvent>()
    val navigationEvents: SharedFlow<CreateQrNavigationEvent> = _navigationEvents.asSharedFlow()

    init {
        loadDepartments()
    }

    fun onLabelChange(label: String) {
        _state.update { it.copy(label = label) }
    }

    fun onDepartmentSelect(departmentId: String) {
        _state.update { it.copy(selectedDepartmentId = departmentId) }
    }

    fun onCreateClick() {
        val deptId = _state.value.selectedDepartmentId
        val label = _state.value.label
        
        if (deptId.isBlank() || label.isBlank()) {
            _state.update { it.copy(error = "Заполните все поля") }
            return
        }

        screenModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            postQrCodeUseCase(deptId, label).onSuccess {
                _state.update { it.copy(isLoading = false, isSuccess = true) }
                _navigationEvents.emit(CreateQrNavigationEvent.NavigateBack)
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }

    private fun loadDepartments() {
        screenModelScope.launch {
            getDepartmentsUseCase().onSuccess { departments ->
                _state.update { it.copy(departments = departments) }
            }.onFailure { error ->
                _state.update { it.copy(error = error.message) }
            }
        }
    }
}
