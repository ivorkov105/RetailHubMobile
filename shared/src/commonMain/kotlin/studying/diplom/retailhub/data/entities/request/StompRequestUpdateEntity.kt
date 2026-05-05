package studying.diplom.retailhub.data.entities.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StompRequestUpdateEntity(
    @SerialName("event_type")
    val eventType: String,
    @SerialName("request_id")
    val requestId: String,
    @SerialName("store_id")
    val storeId: String,
    @SerialName("department_id")
    val departmentId: String,
    @SerialName("department_name")
    val departmentName: String,
    @SerialName("status")
    val status: String,
    @SerialName("assigned_user_id")
    val assignedUserId: String? = null,
    @SerialName("assigned_user_name")
    val assignedUserName: String? = null,
    @SerialName("client_session_token")
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
