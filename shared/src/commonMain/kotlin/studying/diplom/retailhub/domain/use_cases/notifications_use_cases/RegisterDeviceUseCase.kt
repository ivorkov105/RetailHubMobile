package studying.diplom.retailhub.domain.use_cases.notifications_use_cases

import studying.diplom.retailhub.domain.models.devices.DeviceModel
import studying.diplom.retailhub.domain.repositories.DeviceRepository

class RegisterDeviceUseCase(
    private val repository: DeviceRepository
) {
    suspend operator fun invoke(device: DeviceModel): Result<Unit> = repository.registerDevice(device)
}
