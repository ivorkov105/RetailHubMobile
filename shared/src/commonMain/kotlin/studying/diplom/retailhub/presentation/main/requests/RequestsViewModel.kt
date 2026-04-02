package studying.diplom.retailhub.presentation.main.requests

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import studying.diplom.retailhub.domain.use_cases.auth_use_cases.GetProfileUseCase
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.AssignRequestUseCase
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.CompleteRequestUseCase
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.GetRequestsUseCase

class RequestsViewModel(
    private val getRequestsUseCase: GetRequestsUseCase,
    private val assignRequestUseCase: AssignRequestUseCase,
    private val completeRequestUseCase: CompleteRequestUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(RequestsState())
    val state: StateFlow<RequestsState> = _state.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        screenModelScope.launch {
            getProfileUseCase().onSuccess { user ->
                _state.update { it.copy(currentUserFullName = "${user.firstName} ${user.lastName}") }
            }
        }
    }

    fun onEvent(event: RequestsEvent) {
        when (event) {
            is RequestsEvent.OnLoadRequestsList -> loadRequests(isRefresh = true)
            is RequestsEvent.OnShowAcceptDialog -> {
                _state.update { it.copy(requestToAccept = event.request) }
            }
            is RequestsEvent.OnDismissAcceptDialog -> {
                _state.update { it.copy(requestToAccept = null) }
            }
            is RequestsEvent.OnShowCompleteDialog -> {
                _state.update { it.copy(requestToComplete = event.request) }
            }
            is RequestsEvent.OnDismissCompleteDialog -> {
                _state.update { it.copy(requestToComplete = null) }
            }
            is RequestsEvent.OnAcceptRequest -> acceptRequest(event.requestId)
            is RequestsEvent.OnCompleteRequest -> completeRequest(event.requestId)
            is RequestsEvent.OnDismissErrorDialog -> {
                _state.update { it.copy(error = null) }
                loadRequests(isRefresh = true)
            }
        }
    }

    private fun loadRequests(isRefresh: Boolean = true) {
        if (isRefresh) {
            _state.update { it.copy(currentPage = 0, isLastPage = false, requests = emptyList()) }
        }

        if (_state.value.isLastPage) return

        screenModelScope.launch {
            if (isRefresh) {
                _state.update { it.copy(isLoading = true, error = null) }
            } else {
                _state.update { it.copy(isPaginationLoading = true) }
            }
            
            val currentState = _state.value
            
            getRequestsUseCase(
                status = currentState.statusFilter,
                departmentId = currentState.departmentFilter,
                dateFrom = currentState.dateFromFilter,
                dateTo = currentState.dateToFilter,
                page = currentState.currentPage,
                size = currentState.pageSize
            ).onSuccess { result ->
                _state.update { state ->
                    val updatedRequests = if (isRefresh) result else state.requests + result
                    state.copy(
                        requests = updatedRequests,
                        isLoading = false,
                        isPaginationLoading = false,
                        isLastPage = result.size < state.pageSize,
                        currentPage = state.currentPage + 1
                    )
                }
            }.onFailure { throwable ->
                _state.update { it.copy(
                    isLoading = false,
                    isPaginationLoading = false,
                    error = throwable.message ?: "Произошла ошибка при загрузке заявок"
                ) }
            }
        }
    }

    fun loadNextPage() {
        if (!_state.value.isLoading && !_state.value.isPaginationLoading && !_state.value.isLastPage) {
            loadRequests(isRefresh = false)
        }
    }

    private fun acceptRequest(requestId: String) {
        _state.update { it.copy(requestToAccept = null, isLoading = true) }
        screenModelScope.launch {
            assignRequestUseCase(requestId).onSuccess {
                loadRequests(isRefresh = true)
            }.onFailure { throwable ->
                _state.update { it.copy(
                    isLoading = false,
                    error = throwable.message ?: "Не удалось принять заявку"
                ) }
            }
        }
    }

    private fun completeRequest(requestId: String) {
        _state.update { it.copy(requestToComplete = null, isLoading = true) }
        screenModelScope.launch {
            completeRequestUseCase(requestId).onSuccess {
                loadRequests(isRefresh = true)
            }.onFailure { throwable ->
                _state.update { it.copy(
                    isLoading = false,
                    error = throwable.message ?: "Не удалось завершить заявку"
                ) }
            }
        }
    }

    fun onStatusFilterChanged(status: String) {
        _state.update { it.copy(statusFilter = status) }
        loadRequests()
    }

    fun onDepartmentFilterChanged(departmentId: String) {
        _state.update { it.copy(departmentFilter = departmentId) }
        loadRequests()
    }
}
