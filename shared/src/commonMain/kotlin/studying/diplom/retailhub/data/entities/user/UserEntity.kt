package studying.diplom.retailhub.data.entities.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import studying.diplom.retailhub.data.entities.shop.DepartmentEntity

@Serializable
data class UserEntity(
    @SerialName("id")
    val id: String = "",
    @SerialName("store_id")
    val storeId: String = "",
    @SerialName("phone_number")
    val phoneNumber: String = "",
    @SerialName("first_name")
    val firstName: String = "",
    @SerialName("last_name")
    val lastName: String = "",
    @SerialName("role")
    val role: String = "",
    @SerialName("current_status")
    val currentStatus: String = "ACTIVE",
    @SerialName("departments")
    val departments: List<DepartmentEntity> = emptyList(),
    @SerialName("password")
    val password: String = "",
    @SerialName("created_at")
    val createdAt: String = ""
)
