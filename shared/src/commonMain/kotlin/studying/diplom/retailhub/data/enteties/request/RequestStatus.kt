package studying.diplom.retailhub.data.enteties.request

import kotlinx.serialization.Serializable

@Serializable
enum class RequestStatus {
	CREATED,
	IN_PROGRESS,
	COMPLETED,
	CANCELED
}