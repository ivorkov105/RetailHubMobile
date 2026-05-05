package studying.diplom.retailhub.data.entities.qr_codes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QREntity(
	@SerialName("id")
    val id: String,
	@SerialName("department_id")
    val departmentId: String,
	@SerialName("department_name")
	val departmentName: String,
	@SerialName("token")
	val token: String,
	@SerialName("scan_url")
	val scanUrl: String,
	@SerialName("label")
    val label: String,
	@SerialName("is_active")
    val isActive: Boolean,
	@SerialName("created_at")
    val createdAt: String
)
