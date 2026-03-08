package studying.diplom.retailhub.data.enteties.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssignedRequestUserEntity(

	@SerialName("first_name")
	val firstName: String = "",

	@SerialName("last_name")
	val lastName: String = "",

)
