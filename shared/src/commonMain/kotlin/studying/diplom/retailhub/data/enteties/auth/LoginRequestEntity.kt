package studying.diplom.retailhub.data.enteties.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestEntity(
    @SerialName("phone_number")
    val phoneNumber: String,
    @SerialName("password")
    val password: String
)
