package studying.diplom.retailhub.domain.repositories

import studying.diplom.retailhub.domain.models.devices.DeviceModel

interface DeviceRepository {
    suspend fun registerDevice(device: DeviceModel): Result<Unit>
    suspend fun deleteDevice(deviceId: String): Result<Unit>
    fun getRegisteredDeviceId(): String?
}
