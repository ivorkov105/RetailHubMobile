package studying.diplom.retailhub.data.enteties.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestListEntity(
	@SerialName("content")
	val content: List<RequestEntity>,
	@SerialName("page")
	val page: Int,
	@SerialName("size")
	val size: Int,
	@SerialName("total_elements")
	val totalElements: Int,
	@SerialName("total_pages")
	val totalPages: Int
)
