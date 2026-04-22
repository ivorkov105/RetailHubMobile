package studying.diplom.retailhub.domain.models.qr_code

data class QrCodeModel(
    val id: String,
    val departmentId: String,
    val departmentName: String,
    val token: String,
    val scanUrl: String,
    val label: String,
    val isActive: Boolean,
    val createdAt: String
)
