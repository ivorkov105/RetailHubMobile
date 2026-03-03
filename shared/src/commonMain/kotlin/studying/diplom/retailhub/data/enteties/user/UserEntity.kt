package studying.diplom.retailhub.data.enteties.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    @SerialName("id")
    val id: String,
    @SerialName("store_id")
    val storeId: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("role")
    val role: String,
    @SerialName("current_status")
    val currentStatus: String,
    @SerialName("departments")
    val departments: List<DepartmentEntity>,
    @SerialName("created_at")
    val createdAt: String
)

@Serializable
data class DepartmentEntity(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String
)
