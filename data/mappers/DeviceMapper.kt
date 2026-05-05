package studying.diplom.retailhub.data.mappers

import studying.diplom.retailhub.data.entities.devices.registration.DeviceRegistrationRequest
import studying.diplom.retailhub.domain.models.devices.DeviceModel

fun DeviceModel.toRegistrationRequest() = DeviceRegistrationRequest(
    fcmToken = fcmToken,
    deviceInfo = deviceInfo
)
