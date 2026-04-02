package studying.diplom.retailhub.data.enteties.qr_codes

import kotlinx.serialization.Serializable

@Serializable
data class QREntity(
    val id: String,
    val storeId: String,
    val departmentId: String,
    val label: String,
    val url: String,
    val createdAt: String
)
