package studying.diplom.retailhub.data.mappers

import studying.diplom.retailhub.data.enteties.devices.DeviceEntity
import studying.diplom.retailhub.domain.models.devices.DeviceModel

fun DeviceEntity.toModel() = DeviceModel(
    deviceId = deviceId,
    fcmToken = fcmToken
)

fun DeviceModel.toEntity() = DeviceEntity(
    deviceId = deviceId,
    fcmToken = fcmToken
)
