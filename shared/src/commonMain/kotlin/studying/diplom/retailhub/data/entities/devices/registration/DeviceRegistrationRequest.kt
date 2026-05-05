package studying.diplom.retailhub.data.entities.devices.registration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceRegistrationRequest(
    @SerialName("fcm_token")
    val fcmToken: String,
    @SerialName("device_info")
    val deviceInfo: String? = null
)
