package studying.diplom.retailhub.data.repositories

import studying.diplom.retailhub.data.data_sources.LocalSource
import studying.diplom.retailhub.data.data_sources.RemoteSource
import studying.diplom.retailhub.data.mappers.toRegistrationRequest
import studying.diplom.retailhub.domain.models.devices.DeviceModel
import studying.diplom.retailhub.domain.repositories.DeviceRepository

class DeviceRepositoryImpl(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource
) : DeviceRepository {
    override suspend fun registerDevice(device: DeviceModel): Result<Unit> {
        return remoteSource.registerDevice(device.toRegistrationRequest()).map { response ->
            localSource.saveDevice(response.id, response.fcmToken)
        }
    }

    override suspend fun deleteDevice(deviceId: String): Result<Unit> {
        return remoteSource.deleteDevice(deviceId).onSuccess {
            localSource.clearDevice()
        }
    }

    override fun getRegisteredDeviceId(): String? {
        return localSource.getDevice()?.id
    }
}
