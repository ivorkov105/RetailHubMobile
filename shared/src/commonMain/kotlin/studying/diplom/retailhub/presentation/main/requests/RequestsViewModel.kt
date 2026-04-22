package studying.diplom.retailhub.presentation.main.requests

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import studying.diplom.retailhub.data.data_sources.api.ApiException
import studying.diplom.retailhub.domain.repositories.RequestRepository
import studying.diplom.retailhub.domain.use_cases.auth_use_cases.GetProfileUseCase
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.AssignRequestUseCase
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.CompleteRequestUseCase
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.GetRequestsUseCase
import studying.diplom.retailhub.domain.use_cases.shift_use_cases.StartShiftUseCase
import studying.diplom.retailhub.presentation.main.utils.UserRoles

class RequestsViewModel(
    private val getRequestsUseCase: GetRequestsUseCase,
    private val assignRequestUseCase: AssignRequestUseCase,
    private val completeRequestUseCase: CompleteRequestUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val startShiftUseCase: StartShiftUseCase,
    private val requestRepository: RequestRepository
) : ScreenModel {

    private val _state = MutableStateFlow(RequestsState())
    val state: StateFlow<RequestsState> = _state.asStateFlow()

    init {
        loadProfile()
        observeWebSocketUpdates()
    }

    private fun loadProfile() {
        screenModelScope.launch {
            getProfileUseCase().onSuccess { user ->
                _state.update { it.copy(
                    currentUserId = user.id,
                    currentUserFullName = "${user.firstName} ${user.lastName}" 
                ) }
                
                requestRepository.connectToWebSocket()
                
                if (user.role == UserRoles.MANAGER.name) {
                    requestRepository.subscribeToStore(user.storeId)
                } else if (user.role == UserRoles.CONSULTANT.name) {
                    user.departments.forEach { dept ->
                        requestRepository.subscribeToDepartment(dept.id)
                    }
                }
            }
        }
    }

    private fun observeWebSocketUpdates() {
        requestRepository.observeRequestUpdates()
            .onEach { updatedRequest ->
                _state.update { state ->
                    val newList = state.requests.toMutableList()
                    val index = newList.indexOfFirst { it.id == updatedRequest.id }
                    if (index != -1) {
                        newList[index] = updatedRequest
                    } else {
                        newList.add(0, updatedRequest)
                    }
                    state.copy(requests = newList)
                }
            }
            .launchIn(screenModelScope)
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
            is RequestsEvent.OnConfirmStartShift -> startShift()
            is RequestsEvent.OnDismissStartShiftDialog -> {
                _state.update { it.copy(showStartShiftDialog = false) }
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
                if (throwable is ApiException && (throwable.statusCode == HttpStatusCode.BadRequest || throwable.statusCode == HttpStatusCode.PreconditionFailed)) {
                    _state.update { it.copy(isLoading = false, showStartShiftDialog = true) }
                } else {
                    _state.update { it.copy(
                        isLoading = false,
                        error = throwable.message ?: "Не удалось принять заявку"
                    ) }
                }
            }
        }
    }

    private fun startShift() {
        _state.update { it.copy(showStartShiftDialog = false, isLoading = true) }
        screenModelScope.launch {
            startShiftUseCase().onSuccess {
                _state.update { it.copy(isLoading = false) }
                loadRequests(isRefresh = true)
            }.onFailure { throwable ->
                _state.update { it.copy(
                    isLoading = false,
                    error = throwable.message ?: "Не удалось начать смену"
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
                if (throwable is ApiException && (throwable.statusCode == HttpStatusCode.BadRequest || throwable.statusCode == HttpStatusCode.PreconditionFailed)) {
                    _state.update { it.copy(isLoading = false, showStartShiftDialog = true) }
                } else {
                    _state.update { it.copy(
                        isLoading = false,
                        error = throwable.message ?: "Не удалось завершить заявку"
                    ) }
                }
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

    override fun onDispose() {
        requestRepository.disconnectFromWebSocket()
        super.onDispose()
    }
}
