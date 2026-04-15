package studying.diplom.retailhub.data.repositories

import studying.diplom.retailhub.data.data_sources.RemoteSource
import studying.diplom.retailhub.data.mappers.toEntity
import studying.diplom.retailhub.domain.models.devices.DeviceModel
import studying.diplom.retailhub.domain.repositories.DeviceRepository

class DeviceRepositoryImpl(
    private val remoteSource: RemoteSource
) : DeviceRepository {
    override suspend fun registerDevice(device: DeviceModel): Result<Unit> {
        return remoteSource.registerDevice(device.toEntity())
    }

    override suspend fun deleteDevice(deviceId: String): Result<Unit> {
        return remoteSource.deleteDevice(deviceId)
    }
}
