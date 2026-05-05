package studying.diplom.retailhub.data.entities.shop

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoreEntity(

	@SerialName("id")
	val id: String = "",

	@SerialName("name")
	val name: String = "",

	@SerialName("address")
	val address: String = "",

	@SerialName("timezone")
	val timezone: String = "Europe/Moscow",

	@SerialName("created_at")
	val createdAt: String = "",
)
