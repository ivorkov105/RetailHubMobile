package studying.diplom.retailhub.data.entities.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssignedRequestUserEntity(
    @SerialName("id")
    val id: String = "",
    @SerialName("first_name")
    val firstName: String = "",
    @SerialName("last_name")
    val lastName: String = ""
)
