package studying.diplom.retailhub.domain.use_cases.auth_use_cases

import studying.diplom.retailhub.domain.repositories.AuthRepository
import studying.diplom.retailhub.domain.use_cases.notifications_use_cases.UnregisterDeviceUseCase

class LogoutUseCase(
    private val repository: AuthRepository,
    private val unregisterDeviceUseCase: UnregisterDeviceUseCase
) {
    suspend operator fun invoke() {
        unregisterDeviceUseCase()
        repository.logout()
    }
}
