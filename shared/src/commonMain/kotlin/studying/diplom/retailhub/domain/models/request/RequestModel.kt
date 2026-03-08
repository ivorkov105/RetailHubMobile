package studying.diplom.retailhub.domain.models.request

data class RequestModel(
	val id: String,
	val storeId: String,
	val departmentId: String,
	val departmentName: String,
	val isEscalated: Boolean,
	val assignedUserFirstName: String?,
	val assignedUserLastName: String?,
	val status: RequestStatus,
	val clientSessionToken: String,
	val createdAt: String,
	val assignedAt: String?,
	val completedAt: String?
)
