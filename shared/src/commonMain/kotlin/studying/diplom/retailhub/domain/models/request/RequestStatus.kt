package studying.diplom.retailhub.domain.models.request

import kotlinx.serialization.Serializable

@Serializable
enum class RequestStatus {
	CREATED,
	IN_PROGRESS,
	COMPLETED,
	CANCELED
}