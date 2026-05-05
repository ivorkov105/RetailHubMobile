package studying.diplom.retailhub.presentation.main.qr.qr_list

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import studying.diplom.retailhub.domain.models.qr_code.QrCodeModel
import studying.diplom.retailhub.domain.use_cases.store_use_cases.DeleteQrCodeUseCase
import studying.diplom.retailhub.domain.use_cases.store_use_cases.DownloadQrCodeUseCase
import studying.diplom.retailhub.domain.use_cases.store_use_cases.GetQrCodesUseCase
import studying.diplom.retailhub.presentation.main.utils.ImageSaver

class QrListViewModel(
    private val getQrCodesUseCase: GetQrCodesUseCase,
    private val deleteQrCodeUseCase: DeleteQrCodeUseCase,
    private val downloadQrCodeUseCase: DownloadQrCodeUseCase,
    private val imageSaver: ImageSaver
) : ScreenModel {

    private val _state = MutableStateFlow(QrListState())
    val state: StateFlow<QrListState> = _state.asStateFlow()

    private var allQrCodes: List<QrCodeModel> = emptyList()

    init {
        loadQrCodes()
    }

    fun loadQrCodes() {
        screenModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getQrCodesUseCase().onSuccess { list ->
                allQrCodes = list
                updateFilteredList()
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }

    fun toggleFilter() {
        _state.update { it.copy(isFilterActive = !it.isFilterActive) }
        updateFilteredList()
    }

    private fun updateFilteredList() {
        val filtered = if (_state.value.isFilterActive) {
            allQrCodes.filter { it.isActive }
        } else {
            allQrCodes
        }
        _state.update { it.copy(qrCodes = filtered, isLoading = false) }
    }

    fun toggleExpand(id: String) {
        _state.update { 
            it.copy(expandedQrCodeId = if (it.expandedQrCodeId == id) null else id)
        }
    }

    fun deactivateQrCode(id: String) {
        screenModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            deleteQrCodeUseCase(id).onSuccess {
                loadQrCodes()
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }

    fun downloadQrCode(id: String) {
        val qrCode = allQrCodes.find { it.id == id } ?: return
        screenModelScope.launch {
            downloadQrCodeUseCase(id).onSuccess { bytes ->
                try {
                    imageSaver.saveImage(bytes, "qr_${qrCode.label}")
                    showStatus(DownloadStatus.Success)
                } catch (e: Exception) {
                    showStatus(DownloadStatus.Error(e.message ?: "Ошибка сохранения"))
                }
            }.onFailure { error ->
                showStatus(DownloadStatus.Error(error.message ?: "Ошибка сервера"))
            }
        }
    }

    private fun showStatus(status: DownloadStatus) {
        screenModelScope.launch {
            _state.update { it.copy(downloadStatus = status) }
            delay(1000)
            _state.update { it.copy(downloadStatus = null) }
        }
    }

    fun clearStatus() {
        _state.update { it.copy(downloadStatus = null) }
    }
}
