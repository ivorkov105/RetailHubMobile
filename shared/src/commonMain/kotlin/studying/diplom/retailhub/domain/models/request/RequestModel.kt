package studying.diplom.retailhub.domain.models.request

import studying.diplom.retailhub.data.enteties.request.RequestStatus

data class RequestModel(
	val id: String,
	val clientSessionToken: String,
	val status: RequestStatus,
	val departmentName: String?,
	val consultantName: String?,
	val canRemind: Boolean,
	val canReassign: Boolean,
	val createdAt: String,
	val assignedAt: String?
)