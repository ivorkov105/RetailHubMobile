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
import studying.diplom.retailhub.domain.models.request.RequestStatus
import studying.diplom.retailhub.domain.repositories.RequestRepository
import studying.diplom.retailhub.domain.use_cases.auth_use_cases.GetProfileUseCase
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.AssignRequestUseCase
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.CompleteRequestUseCase
import studying.diplom.retailhub.domain.use_cases.shift_use_cases.StartShiftUseCase
import studying.diplom.retailhub.presentation.main.utils.UserRoles

class RequestsViewModel(
	private val assignRequestUseCase: AssignRequestUseCase,
	private val completeRequestUseCase: CompleteRequestUseCase,
	private val getProfileUseCase: GetProfileUseCase,
	private val startShiftUseCase: StartShiftUseCase,
	private val requestRepository: RequestRepository
) : ScreenModel {

	private val _state = MutableStateFlow(RequestsState())
	val state: StateFlow<RequestsState> = _state.asStateFlow()

	private var lastStoreId: String? = null
	private var lastDeptIds: List<String> = emptyList()

	init {
		loadProfile()
		observeWebSocketUpdates()
	}

	private fun loadProfile() {
		screenModelScope.launch {
			_state.update { it.copy(isLoading = true, error = null) }
			getProfileUseCase().onSuccess { user ->
				_state.update {
					it.copy(
						currentUserId = user.id,
						currentUserFullName = "${user.firstName} ${user.lastName}",
						currentUserRole = user.role,
						currentUserDepartmentIds = user.departments.map { it.id }
					)
				}

				loadRequests().onSuccess {
					requestRepository.connectToWebSocket()

					if (user.role == UserRoles.MANAGER.name) {
						lastStoreId = user.storeId
						requestRepository.subscribeToStore(user.storeId)
					} else if (user.role == UserRoles.CONSULTANT.name) {
						lastDeptIds = user.departments.map { it.id }
						user.departments.forEach { dept ->
							requestRepository.subscribeToDepartment(dept.id)
						}
					}
				}
			}.onFailure { throwable ->
				_state.update { it.copy(isLoading = false, error = throwable.message ?: "Ошибка загрузки профиля") }
			}
		}
	}

	private suspend fun loadRequests(): Result<Unit> {
		val currentState = _state.value
		val requests = requestRepository.getRequests(
			status = currentState.filterStatus?.name,
			departmentId = currentState.filterDepartmentId.ifBlank { null },
			dateFrom = currentState.filterDateFrom,
			dateTo = currentState.filterDateTo
		)

		return requests.fold(
			onSuccess = { fetchedRequests ->
				var filtered = if (currentState.filterStatus == null) {
					fetchedRequests.filter { it.status != RequestStatus.COMPLETED && it.status != RequestStatus.CANCELED }
				} else {
					fetchedRequests
				}

				if (currentState.currentUserRole == UserRoles.CONSULTANT.name) {
					filtered = filtered.filter { it.departmentId in currentState.currentUserDepartmentIds }
				}

				_state.update { it.copy(requests = filtered, isLoading = false) }
				Result.success(Unit)
			},
			onFailure = { throwable ->
				_state.update { it.copy(isLoading = false, error = throwable.message ?: "Ошибка загрузки списка заявок") }
				Result.failure(throwable)
			}
		)
	}

	private fun observeWebSocketUpdates() {
		requestRepository.observeRequestUpdates()
			.onEach { updatedRequest ->
				_state.update { state ->
					val newList = state.requests.toMutableList()
					val index = newList.indexOfFirst { it.id == updatedRequest.id }

					val matchesStatus = when {
						state.filterStatus != null -> updatedRequest.status == state.filterStatus
						else -> updatedRequest.status != RequestStatus.COMPLETED && updatedRequest.status != RequestStatus.CANCELED
					}
					val matchesDept = state.filterDepartmentId.isBlank() || updatedRequest.departmentId == state.filterDepartmentId
					val matchesUserDept = state.currentUserRole != UserRoles.CONSULTANT.name ||
							updatedRequest.departmentId in state.currentUserDepartmentIds

					val matchesFilters = matchesStatus && matchesDept && matchesUserDept

					if (index != -1) {
						if (matchesFilters) {
							newList[index] = updatedRequest
						} else {
							newList.removeAt(index)
						}
					} else {
						if (matchesFilters) {
							newList.add(0, updatedRequest)
						}
					}
					state.copy(requests = newList)
				}
			}
			.launchIn(screenModelScope)
	}

	fun onEvent(event: RequestsEvent) {
		when (event) {
			is RequestsEvent.OnShowAcceptDialog        -> {
				_state.update { it.copy(requestToAccept = event.request) }
			}

			is RequestsEvent.OnDismissAcceptDialog     -> {
				_state.update { it.copy(requestToAccept = null) }
			}

			is RequestsEvent.OnShowCompleteDialog      -> {
				_state.update { it.copy(requestToComplete = event.request) }
			}

			is RequestsEvent.OnDismissCompleteDialog   -> {
				_state.update { it.copy(requestToComplete = null) }
			}

			is RequestsEvent.OnAcceptRequest           -> acceptRequest(event.requestId)
			is RequestsEvent.OnCompleteRequest         -> completeRequest(event.requestId)

			is RequestsEvent.OnDismissErrorDialog      -> {
				_state.update { it.copy(error = null) }
			}

			is RequestsEvent.OnConfirmStartShift       -> startShift()

			is RequestsEvent.OnDismissStartShiftDialog -> {
				_state.update { it.copy(showStartShiftDialog = false) }
			}

			is RequestsEvent.OnRetryLoad               -> loadProfile()

			RequestsEvent.OnToggleFilterDialog -> {
				_state.update { it.copy(showFilterDialog = !it.showFilterDialog) }
			}
			is RequestsEvent.OnFilterStatusChange -> {
				_state.update { it.copy(filterStatus = event.status) }
			}
			is RequestsEvent.OnFilterDepartmentIdChange -> {
				_state.update { it.copy(filterDepartmentId = event.departmentId) }
			}
			is RequestsEvent.OnFilterDateFromChange -> {
				_state.update { it.copy(filterDateFrom = event.date) }
			}
			is RequestsEvent.OnFilterDateToChange -> {
				_state.update { it.copy(filterDateTo = event.date) }
			}
			RequestsEvent.OnApplyFilters -> {
				_state.update { it.copy(showFilterDialog = false, isLoading = true) }
				screenModelScope.launch {
					loadRequests()
				}
			}
			RequestsEvent.OnClearFilters -> {
				_state.update {
					it.copy(
						filterStatus = null,
						filterDepartmentId = "",
						filterDateFrom = null,
						filterDateTo = null,
						showFilterDialog = false,
						isLoading = true
					)
				}
				screenModelScope.launch {
					loadRequests()
				}
			}
		}
	}

	private fun acceptRequest(requestId: String) {
		_state.update { it.copy(requestToAccept = null, isLoading = true) }
		screenModelScope.launch {
			assignRequestUseCase(requestId).onSuccess {
				_state.update { it.copy(isLoading = false) }
			}.onFailure { throwable ->
				if (throwable is ApiException && (throwable.statusCode == HttpStatusCode.BadRequest || throwable.statusCode == HttpStatusCode.PreconditionFailed)) {
					_state.update { it.copy(isLoading = false, showStartShiftDialog = true) }
				} else {
					_state.update {
						it.copy(
							isLoading = false,
							error = throwable.message ?: "Не удалось принять заявку"
						)
					}
				}
			}
		}
	}

	private fun startShift() {
		_state.update { it.copy(showStartShiftDialog = false, isLoading = true) }
		screenModelScope.launch {
			startShiftUseCase().onSuccess {
				_state.update { it.copy(isLoading = false) }
			}.onFailure { throwable ->
				_state.update {
					it.copy(
						isLoading = false,
						error = throwable.message ?: "Не удалось начать смену"
					)
				}
			}
		}
	}

	private fun completeRequest(requestId: String) {
		_state.update { it.copy(requestToComplete = null, isLoading = true) }
		screenModelScope.launch {
			completeRequestUseCase(requestId).onSuccess {
				_state.update { it.copy(isLoading = false) }
			}.onFailure { throwable ->
				if (throwable is ApiException && (throwable.statusCode == HttpStatusCode.BadRequest || throwable.statusCode == HttpStatusCode.PreconditionFailed)) {
					_state.update { it.copy(isLoading = false, showStartShiftDialog = true) }
				} else {
					_state.update {
						it.copy(
							isLoading = false,
							error = throwable.message ?: "Не удалось завершить заявку"
						)
					}
				}
			}
		}
	}

	override fun onDispose() {
		lastStoreId?.let { requestRepository.unsubscribeFromStore(it) }
		lastDeptIds.forEach { requestRepository.unsubscribeFromDepartment(it) }
		super.onDispose()
	}
}
