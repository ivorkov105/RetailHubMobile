package studying.diplom.retailhub.data.enteties.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestEntity(

	@SerialName("id")
	val id: String = "",

	@SerialName("store_id")
	val storeId: String = "",

	@SerialName("department_id")
	val departmentId: String = "",

	@SerialName("department_name")
	val departmentName: String = "",

	@SerialName("is_escalated")
	val isEscalated: Boolean = false,

	@SerialName("assigned_user")
	val assignedUser: AssignedRequestUserEntity = AssignedRequestUserEntity(),

	@SerialName("status")
	val status: String = RequestStatus.CREATED.name,

	@SerialName("client_session_token")
	val clientSessionToken: String = "",

	@SerialName("created_at")
	val createdAt: String = "",

	@SerialName("assigned_at")
	val assignedAt: String = "",

	@SerialName("completed_at")
	val completedAt: String = ""
)
