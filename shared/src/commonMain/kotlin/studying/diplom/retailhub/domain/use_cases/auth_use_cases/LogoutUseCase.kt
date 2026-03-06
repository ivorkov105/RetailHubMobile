package studying.diplom.retailhub.domain.use_cases.auth_use_cases

import studying.diplom.retailhub.domain.repositories.AuthRepository

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}
