package studying.diplom.retailhub.data.enteties.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserListEntity(
    @SerialName("content")
    val content: List<UserEntity>,
    @SerialName("page")
    val page: Int,
    @SerialName("size")
    val size: Int,
    @SerialName("total_elements")
    val totalElements: Int,
    @SerialName("total_pages")
    val totalPages: Int
)
