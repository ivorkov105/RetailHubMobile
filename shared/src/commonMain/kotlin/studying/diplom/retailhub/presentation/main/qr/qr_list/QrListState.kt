package studying.diplom.retailhub.presentation.main.qr.qr_list

import studying.diplom.retailhub.domain.models.qr_code.QrCodeModel

data class QrListState(
    val qrCodes: List<QrCodeModel> = emptyList(),
    val expandedQrCodeId: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val downloadStatus: DownloadStatus? = null,
    val isFilterActive: Boolean = true
)

sealed class DownloadStatus {
    object Success : DownloadStatus()
    data class Error(val message: String) : DownloadStatus()
}
