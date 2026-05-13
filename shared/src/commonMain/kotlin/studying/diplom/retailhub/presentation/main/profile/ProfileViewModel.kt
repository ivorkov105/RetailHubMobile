package studying.diplom.retailhub.presentation.main.profile

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
import studying.diplom.retailhub.domain.models.analytics.Period
import studying.diplom.retailhub.domain.models.shop.StoreModel
import studying.diplom.retailhub.domain.use_cases.analytics_use_cases.GetAnalyticsDashboardUseCase
import studying.diplom.retailhub.domain.use_cases.auth_use_cases.GetProfileUseCase
import studying.diplom.retailhub.domain.use_cases.auth_use_cases.LogoutUseCase
import studying.diplom.retailhub.domain.use_cases.shift_use_cases.EndShiftUseCase
import studying.diplom.retailhub.domain.use_cases.shift_use_cases.GetMyShiftsUseCase
import studying.diplom.retailhub.domain.use_cases.shift_use_cases.StartShiftUseCase
import studying.diplom.retailhub.domain.use_cases.store_use_cases.GetDepartmentsUseCase
import studying.diplom.retailhub.domain.use_cases.store_use_cases.GetMyStoreUseCase
import studying.diplom.retailhub.presentation.main.profile.ProfileNavigationEvent.*
import studying.diplom.retailhub.presentation.main.utils.UserRoles

sealed class ProfileNavigationEvent {
	object NavigateToAuth : ProfileNavigationEvent()
	object NavigateToMyStore : ProfileNavigationEvent()
	object NavigateToCreateStore : ProfileNavigationEvent()
	object NavigateToCreateDepartment : ProfileNavigationEvent()
	object NavigateToCreateEmployee : ProfileNavigationEvent()
	object NavigateToCreateQr : ProfileNavigationEvent()
	object NavigateToQrList : ProfileNavigationEvent()
	data class NavigateToUpdateStore(val store: StoreModel) : ProfileNavigationEvent()
}

class ProfileViewModel(
	private val getProfileUseCase: GetProfileUseCase,
	private val getMyStoreUseCase: GetMyStoreUseCase,
	private val getDepartmentsUseCase: GetDepartmentsUseCase,
	private val logoutUseCase: LogoutUseCase,
	private val startShiftUseCase: StartShiftUseCase,
	private val endShiftUseCase: EndShiftUseCase,
	private val getAnalyticsDashboardUseCase: GetAnalyticsDashboardUseCase,
	private val getMyShiftsUseCase: GetMyShiftsUseCase
) : ScreenModel {

	private val _state = MutableStateFlow(ProfileState())
	val state: StateFlow<ProfileState> = _state.asStateFlow()

	private val _navigationEvents = MutableSharedFlow<ProfileNavigationEvent>()
	val navigationEvents: SharedFlow<ProfileNavigationEvent> = _navigationEvents.asSharedFlow()

	fun onEvent(event: ProfileEvent) {
		when (event) {
			is ProfileEvent.LoadProfile          -> loadProfile()
			is ProfileEvent.Logout               -> logout()

			is ProfileEvent.OnMyStoreClick       -> {
				screenModelScope.launch {
					_navigationEvents.emit(NavigateToMyStore)
				}
			}

			is ProfileEvent.OnCreateStoreClick   -> {
				screenModelScope.launch {
					_navigationEvents.emit(NavigateToCreateStore)
				}
			}

			is ProfileEvent.OnUpdateStoreClick   -> {
				screenModelScope.launch {
					_state.value.store?.let {
						_navigationEvents.emit(NavigateToUpdateStore(it))
					}
				}
			}

			ProfileEvent.OnCreateDepartmentClick -> {
				screenModelScope.launch {
					_navigationEvents.emit(NavigateToCreateDepartment)
				}
			}

			ProfileEvent.OnCreateEmployeeClick   -> {
				screenModelScope.launch {
					_navigationEvents.emit(NavigateToCreateEmployee)
				}
			}

			ProfileEvent.OnQrClick               -> {
				screenModelScope.launch {
					_navigationEvents.emit(NavigateToCreateQr)
				}
			}

			ProfileEvent.OnQrListClick           -> {
				screenModelScope.launch {
					_navigationEvents.emit(NavigateToQrList)
				}
			}

			ProfileEvent.OnStartShiftClick       -> startShift()
			ProfileEvent.OnEndShiftClick         -> endShift()

			is ProfileEvent.OnDateFromChange     -> {
				_state.update { it.copy(dateFrom = event.date) }
				loadShifts()
			}

			is ProfileEvent.OnDateToChange       -> {
				_state.update { it.copy(dateTo = event.date) }
				loadShifts()
			}

			ProfileEvent.LoadShifts              -> loadShifts()

            is ProfileEvent.OnPeriodChange -> {
                _state.update { it.copy(selectedPeriod = event.period) }
                loadDashboard(event.period)
            }
		}
	}

	private fun loadProfile() {
		screenModelScope.launch {
			_state.update { it.copy(isLoading = true, error = null, analyticsError = null) }

			val userResult = getProfileUseCase()
			val storeResult = getMyStoreUseCase()
			val departmentsResult = getDepartmentsUseCase()

			if (userResult.isFailure) {
				val error = userResult.exceptionOrNull()
				if (error is ApiException && error.statusCode == HttpStatusCode.Unauthorized) {
					logout()
					return@launch
				}
			}

			val user = userResult.getOrNull()
			
			_state.update {
				it.copy(
					user = user,
					store = storeResult.getOrNull(),
					allDepartments = departmentsResult.getOrNull() ?: emptyList(),
					isLoading = false,
					error = userResult.exceptionOrNull()?.message ?: storeResult.exceptionOrNull()?.message
				)
			}

            if (user?.role?.uppercase() == UserRoles.MANAGER.name) {
                loadDashboard(_state.value.selectedPeriod)
            } else if (user?.role?.uppercase() == UserRoles.CONSULTANT.name) {
				loadShifts()
			}
		}
	}

    private fun loadDashboard(period: Period) {
        screenModelScope.launch {
            _state.update { it.copy(analyticsError = null) }
            getAnalyticsDashboardUseCase(period).onSuccess { dashboard ->
                _state.update { it.copy(dashboard = dashboard) }
            }.onFailure { error ->
                _state.update { it.copy(analyticsError = error.message) }
            }
        }
    }

	private fun loadShifts() {
		val dateFrom = _state.value.dateFrom ?: return
		val dateTo = _state.value.dateTo ?: return

		screenModelScope.launch {
			_state.update { it.copy(isShiftsLoading = true) }
			getMyShiftsUseCase(dateFrom, dateTo).onSuccess { shifts ->
				_state.update { it.copy(shifts = shifts, isShiftsLoading = false) }
			}.onFailure { error ->
				_state.update { it.copy(isShiftsLoading = false, error = error.message) }
			}
		}
	}

	private fun logout() {
		screenModelScope.launch {
			logoutUseCase()
			_navigationEvents.emit(NavigateToAuth)
		}
	}

	private fun startShift() {
		screenModelScope.launch {
			_state.update { it.copy(isLoading = true) }
			startShiftUseCase().onSuccess {
				loadProfile()
			}.onFailure { error ->
				handleShiftError(error)
			}
		}
	}

	private fun endShift() {
		screenModelScope.launch {
			_state.update { it.copy(isLoading = true) }
			endShiftUseCase().onSuccess {
				loadProfile()
			}.onFailure { error ->
				handleShiftError(error)
			}
		}
	}

	private fun handleShiftError(error: Throwable) {
		if (error is ApiException && error.statusCode == HttpStatusCode.Unauthorized) {
			logout()
		} else {
			_state.update { it.copy(isLoading = false, error = error.message) }
		}
	}
}
