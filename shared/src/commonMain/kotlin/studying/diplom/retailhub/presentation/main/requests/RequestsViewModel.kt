package studying.diplom.retailhub.presentation.main.requests

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import studying.diplom.retailhub.domain.models.request.RequestModel
import studying.diplom.retailhub.domain.models.request.RequestStatus
import studying.diplom.retailhub.domain.repositories.RequestRepository
import studying.diplom.retailhub.domain.use_cases.auth_use_cases.GetProfileUseCase
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.AssignRequestUseCase
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.CompleteRequestUseCase
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.GetPagedRequestsUseCase
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.SaveRequestUseCase
import studying.diplom.retailhub.presentation.main.utils.UserRoles

class RequestsViewModel(
	private val assignRequestUseCase: AssignRequestUseCase,
	private val completeRequestUseCase: CompleteRequestUseCase,
	private val getProfileUseCase: GetProfileUseCase,
	private val getPagedRequestsUseCase: GetPagedRequestsUseCase,
	private val saveRequestUseCase: SaveRequestUseCase,
	private val requestRepository: RequestRepository
) : ScreenModel {

	private val _state = MutableStateFlow(RequestsState())
	val state: StateFlow<RequestsState> = _state.asStateFlow()

	@OptIn(ExperimentalCoroutinesApi::class)
	val requestsPagingData: Flow<PagingData<RequestModel>> = combine(
		_state.map { it.filterStatus }.distinctUntilChanged(),
		_state.map { it.filterDepartmentId }.distinctUntilChanged(),
		_state.map { it.filterDateFrom }.distinctUntilChanged(),
		_state.map { it.filterDateTo }.distinctUntilChanged(),
	) { status: RequestStatus?, deptId: String, dateFrom: String?, dateTo: String? ->
		FilterParams(status, deptId, dateFrom, dateTo)
	}.flatMapLatest { params: FilterParams ->
		getPagedRequestsUseCase(
			status = params.status,
			departmentId = params.deptId.ifBlank { null },
			dateFrom = params.dateFrom,
			dateTo = params.dateTo
		).map { pagingData: PagingData<RequestModel> ->
			val currentState = _state.value
			val isConsultant = currentState.currentUserRole == UserRoles.CONSULTANT.name
			val allowedStatuses = setOf(RequestStatus.WAITING, RequestStatus.ESCALATED, RequestStatus.ASSIGNED)

			pagingData.filter { request: RequestModel ->
				val matchesStatus = request.status in allowedStatuses
				val matchesAssignment = request.assignedUserId == null || request.assignedUserId == currentState.currentUserId
				val matchesUserDept = !isConsultant || request.departmentId in currentState.currentUserDepartmentIds
				matchesStatus && matchesAssignment && matchesUserDept
			}
		}
	}.cachedIn(screenModelScope)

	private data class FilterParams(
		val status: RequestStatus?,
		val deptId: String,
		val dateFrom: String?,
		val dateTo: String?
	)

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
				_state.update { state ->
					state.copy(
						currentUserId = user.id,
						currentUserFullName = "${user.firstName} ${user.lastName}",
						currentUserRole = user.role,
						currentUserDepartmentIds = user.departments.map { dept -> dept.id },
						isLoading = false
					)
				}

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
			}.onFailure { throwable ->
				_state.update { it.copy(isLoading = false, error = throwable.message ?: "Ошибка загрузки профиля") }
			}
		}
	}

	private fun observeWebSocketUpdates() {
		requestRepository.observeRequestUpdates()
			.onEach { request ->
				saveRequestUseCase(request)
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
				_state.update { it.copy(showFilterDialog = false) }
			}
			RequestsEvent.OnClearFilters -> {
				_state.update {
					it.copy(
						filterStatus = null,
						filterDepartmentId = "",
						filterDateFrom = null,
						filterDateTo = null,
						showFilterDialog = false
					)
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
				_state.update {
					it.copy(
						isLoading = false,
						error = throwable.message ?: "Не удалось принять заявку"
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
				_state.update {
					it.copy(
						isLoading = false,
						error = throwable.message ?: "Не удалось завершить заявку"
					)
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
