package studying.diplom.retailhub.data.entities.request

import kotlinx.serialization.Serializable

@Serializable
enum class RequestStatus {
	CREATED,
	WAITING,
	ESCALATED,
	ASSIGNED,
	COMPLETED,
	CANCELED
}