package studying.diplom.retailhub.presentation.main.departments.department

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
import studying.diplom.retailhub.domain.models.request.RequestStatus
import studying.diplom.retailhub.domain.models.shop.DepartmentModel
import studying.diplom.retailhub.domain.repositories.RequestRepository
import studying.diplom.retailhub.domain.use_cases.auth_use_cases.GetProfileUseCase
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.AssignRequestUseCase
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.CompleteRequestUseCase
import studying.diplom.retailhub.domain.use_cases.shift_use_cases.StartShiftUseCase
import studying.diplom.retailhub.domain.use_cases.store_use_cases.AddDepartmentUseCase
import studying.diplom.retailhub.domain.use_cases.store_use_cases.DeleteDepartmentUseCase
import studying.diplom.retailhub.domain.use_cases.store_use_cases.GetDepartmentUseCase
import studying.diplom.retailhub.domain.use_cases.store_use_cases.UpdateDepartmentUseCase

sealed class DepartmentNavigationEvent {
	object NavigateBack : DepartmentNavigationEvent()
    object NavigateToAuth : DepartmentNavigationEvent()
}

class DepartmentViewModel(
	private val addDepartmentUseCase: AddDepartmentUseCase,
	private val getDepartmentUseCase: GetDepartmentUseCase,
	private val updateDepartmentUseCase: UpdateDepartmentUseCase,
	private val deleteDepartmentUseCase: DeleteDepartmentUseCase,
    private val requestRepository: RequestRepository,
    private val getProfileUseCase: GetProfileUseCase,
    private val assignRequestUseCase: AssignRequestUseCase,
    private val completeRequestUseCase: CompleteRequestUseCase,
    private val startShiftUseCase: StartShiftUseCase,
	private val initialDepartment: DepartmentModel? = null
) : ScreenModel {

	private val _state = MutableStateFlow(DepartmentState(data = initialDepartment ?: DepartmentModel()))
	val state: StateFlow<DepartmentState> = _state.asStateFlow()

	private val _navigationEvents = MutableSharedFlow<DepartmentNavigationEvent>()
	val navigationEvents: SharedFlow<DepartmentNavigationEvent> = _navigationEvents.asSharedFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        screenModelScope.launch {
            getProfileUseCase().onSuccess { user ->
                _state.update { it.copy(currentUserId = user.id) }
            }
        }
    }

    private fun handleError(throwable: Throwable) {
        if (throwable is ApiException && throwable.statusCode == HttpStatusCode.Unauthorized) {
            screenModelScope.launch {
                _navigationEvents.emit(DepartmentNavigationEvent.NavigateToAuth)
            }
        } else {
            _state.update { it.copy(isLoading = false, error = throwable.message ?: "Произошла ошибка") }
        }
    }

	fun onEvent(event: DepartmentEvent) {
		when (event) {
			is DepartmentEvent.OnLoadDepartment -> {
                loadDepartment()
                loadRequests()
            }
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
            
            // Requests events
            DepartmentEvent.OnToggleFilterDialog -> {
                _state.update { it.copy(showFilterDialog = !it.showFilterDialog) }
            }
            is DepartmentEvent.OnFilterStatusChange -> {
                _state.update { it.copy(filterStatus = event.status) }
            }
            is DepartmentEvent.OnFilterDateFromChange -> {
                _state.update { it.copy(filterDateFrom = event.date) }
            }
            is DepartmentEvent.OnFilterDateToChange -> {
                _state.update { it.copy(filterDateTo = event.date) }
            }
            DepartmentEvent.OnApplyFilters -> {
                _state.update { it.copy(showFilterDialog = false) }
                loadRequests()
            }
            DepartmentEvent.OnClearFilters -> {
                _state.update { 
                    it.copy(
                        filterStatus = null,
                        filterDateFrom = null,
                        filterDateTo = null,
                        showFilterDialog = false
                    )
                }
                loadRequests()
            }
            is DepartmentEvent.OnAcceptRequest -> acceptRequest(event.requestId)
            is DepartmentEvent.OnCompleteRequest -> completeRequest(event.requestId)
            is DepartmentEvent.OnShowAcceptDialog -> _state.update { it.copy(requestToAccept = event.request) }
            DepartmentEvent.OnDismissAcceptDialog -> _state.update { it.copy(requestToAccept = null) }
            is DepartmentEvent.OnShowCompleteDialog -> _state.update { it.copy(requestToComplete = event.request) }
            DepartmentEvent.OnDismissCompleteDialog -> _state.update { it.copy(requestToComplete = null) }
            DepartmentEvent.OnConfirmStartShift -> startShift()
            DepartmentEvent.OnDismissStartShiftDialog -> _state.update { it.copy(showStartShiftDialog = false) }
            DepartmentEvent.OnDismissErrorDialog -> _state.update { it.copy(error = null) }
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
				.onFailure { handleError(it) }
		}
	}

    private fun loadRequests() {
        val departmentId = initialDepartment?.id ?: return
        screenModelScope.launch {
            _state.update { it.copy(isRequestsLoading = true) }
            val currentState = _state.value
            requestRepository.getRequests(
                status = currentState.filterStatus?.name,
                departmentId = departmentId,
                dateFrom = currentState.filterDateFrom,
                dateTo = currentState.filterDateTo
            ).onSuccess { fetchedRequests ->
                val filtered = if (currentState.filterStatus == null) {
					fetchedRequests.filter { it.status != RequestStatus.COMPLETED && it.status != RequestStatus.CANCELED }
				} else {
					fetchedRequests
				}
                _state.update { it.copy(requests = filtered, isRequestsLoading = false) }
            }.onFailure {
                _state.update { it.copy(isRequestsLoading = false) }
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
			}.onFailure { handleError(it) }
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
			}.onFailure { handleError(it) }
		}
	}

	private fun deleteDepartment() {
		val departmentData = _state.value.data ?: return
		screenModelScope.launch {
			_state.update { it.copy(isLoading = true, error = null) }
			deleteDepartmentUseCase(departmentData).onSuccess {
				_state.update { it.copy(isLoading = false) }
				_navigationEvents.emit(DepartmentNavigationEvent.NavigateBack)
			}.onFailure { handleError(it) }
		}
	}

    private fun acceptRequest(requestId: String) {
		_state.update { it.copy(requestToAccept = null, isRequestsLoading = true) }
		screenModelScope.launch {
			assignRequestUseCase(requestId).onSuccess {
				loadRequests()
			}.onFailure { throwable ->
				if (throwable is ApiException && (throwable.statusCode == HttpStatusCode.BadRequest || throwable.statusCode == HttpStatusCode.PreconditionFailed)) {
					_state.update { it.copy(isRequestsLoading = false, showStartShiftDialog = true) }
				} else {
					_state.update {
						it.copy(
							isRequestsLoading = false,
							error = throwable.message ?: "Не удалось принять заявку"
						)
					}
				}
			}
		}
	}

	private fun completeRequest(requestId: String) {
		_state.update { it.copy(requestToComplete = null, isRequestsLoading = true) }
		screenModelScope.launch {
			completeRequestUseCase(requestId).onSuccess {
				loadRequests()
			}.onFailure { throwable ->
				if (throwable is ApiException && (throwable.statusCode == HttpStatusCode.BadRequest || throwable.statusCode == HttpStatusCode.PreconditionFailed)) {
					_state.update { it.copy(isRequestsLoading = false, showStartShiftDialog = true) }
				} else {
					_state.update {
						it.copy(
							isRequestsLoading = false,
							error = throwable.message ?: "Не удалось завершить заявку"
						)
					}
				}
			}
		}
	}

    private fun startShift() {
		_state.update { it.copy(showStartShiftDialog = false, isRequestsLoading = true) }
		screenModelScope.launch {
			startShiftUseCase().onSuccess {
				_state.update { it.copy(isRequestsLoading = false) }
			}.onFailure { throwable ->
				_state.update {
					it.copy(
						isRequestsLoading = false,
						error = throwable.message ?: "Не удалось начать смену"
					)
				}
			}
		}
	}
}
