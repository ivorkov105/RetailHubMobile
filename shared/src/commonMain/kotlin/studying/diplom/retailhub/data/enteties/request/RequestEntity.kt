package studying.diplom.retailhub.data.enteties.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestEntity(
	@SerialName("id")
	val id: String,

	@SerialName("client_session_token")
	val clientSessionToken: String,

	@SerialName("status")
	val status: RequestStatus,

	@SerialName("department_name")
	val departmentName: String?,

	@SerialName("consultant_name")
	val consultantName: String?,

	@SerialName("can_remind")
	val canRemind: Boolean,

	@SerialName("can_reassign")
	val canReassign: Boolean,

	@SerialName("created_at")
	val createdAt: String,

	@SerialName("assigned_at")
	val assignedAt: String?
)
