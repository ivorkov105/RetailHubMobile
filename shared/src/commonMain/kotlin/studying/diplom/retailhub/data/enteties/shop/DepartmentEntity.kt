package studying.diplom.retailhub.data.enteties.shop

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DepartmentEntity(

	@SerialName("id")
	val id: String = "",

	@SerialName("store_id")
	val storeId: String = "",

	@SerialName("name")
	val name: String = "",

	@SerialName("description")
	val description: String = "",

	@SerialName("created_at")
	val createdAt: String = ""
)
