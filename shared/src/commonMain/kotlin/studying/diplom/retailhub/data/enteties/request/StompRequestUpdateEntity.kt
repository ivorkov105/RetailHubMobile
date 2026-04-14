package studying.diplom.retailhub.data.enteties.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StompRequestUpdateEntity(
    @SerialName("eventType")
    val eventType: String,
    @SerialName("requestId")
    val requestId: String,
    @SerialName("storeId")
    val storeId: String,
    @SerialName("departmentId")
    val departmentId: String,
    @SerialName("departmentName")
    val departmentName: String,
    @SerialName("status")
    val status: String,
    @SerialName("assignedUserId")
    val assignedUserId: String? = null,
    @SerialName("assignedUserName")
    val assignedUserName: String? = null,
    @SerialName("clientSessionToken")
    val clientSessionToken: String,
    @SerialName("timestamp")
    val timestamp: Long
)

fun StompRequestUpdateEntity.toRequestEntity(): RequestEntity {
    return RequestEntity(
        id = requestId,
        storeId = storeId,
        departmentId = departmentId,
        departmentName = departmentName,
        status = status,
        clientSessionToken = clientSessionToken,
        // Так как в STOMP сообщении имя и ID приходят отдельно,
        // маппим их в существующую структуру AssignedRequestUserEntity
        assignedUser = if (assignedUserId != null) {
            AssignedRequestUserEntity(
                id = assignedUserId,
                firstName = assignedUserName?.split(" ")?.getOrNull(0) ?: "",
                lastName = assignedUserName?.split(" ")?.getOrNull(1) ?: ""
            )
        } else {
            AssignedRequestUserEntity()
        }
    )
}
