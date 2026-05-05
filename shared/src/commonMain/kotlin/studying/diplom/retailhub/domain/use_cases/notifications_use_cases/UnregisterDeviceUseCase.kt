package studying.diplom.retailhub.domain.use_cases.notifications_use_cases

import studying.diplom.retailhub.domain.repositories.DeviceRepository

class UnregisterDeviceUseCase(
    private val repository: DeviceRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        val deviceId = repository.getRegisteredDeviceId()
        return if (deviceId != null) {
            repository.deleteDevice(deviceId)
        } else {
            Result.success(Unit)
        }
    }
}
