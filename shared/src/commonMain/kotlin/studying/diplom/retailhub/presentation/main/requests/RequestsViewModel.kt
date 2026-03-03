package studying.diplom.retailhub.presentation.main.requests

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import studying.diplom.retailhub.domain.models.RequestModel
import studying.diplom.retailhub.domain.use_cases.requests_use_cases.GetRequestsUseCase

data class RequestsState(
    val requests: List<RequestModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class RequestsViewModel(
    private val getRequestsUseCase: GetRequestsUseCase
) : ScreenModel {

    private val _state = MutableStateFlow(RequestsState())
    val state: StateFlow<RequestsState> = _state.asStateFlow()

    fun loadRequests() {
        screenModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            getRequestsUseCase().onSuccess { result ->
                _state.update { it.copy(requests = result, isLoading = false) }
            }.onFailure { throwable ->
                _state.update { it.copy(
                    isLoading = false,
                    error = throwable.message ?: "Произошла ошибка при загрузке заявок"
                ) }
            }
        }
    }
}
