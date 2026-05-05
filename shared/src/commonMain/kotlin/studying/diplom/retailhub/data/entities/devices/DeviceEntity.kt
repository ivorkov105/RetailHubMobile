package studying.diplom.retailhub.data.entities.devices

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceEntity(
    @SerialName("device_id")
    val deviceId: String,
    @SerialName("fcm_token")
    val fcmToken: String
)
