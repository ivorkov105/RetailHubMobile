package studying.diplom.retailhub.domain.models.request

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
