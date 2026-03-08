package studying.diplom.retailhub.data.enteties.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestListEntity(

	@SerialName("content")
	val content: List<RequestEntity> = emptyList(),

	@SerialName("page")
	val page: Int = 0,

	@SerialName("size")
	val size: Int = 0,

	@SerialName("total_elements")
	val totalElements: Int = 0,

	@SerialName("total_pages")
	val totalPages: Int = 0
)
