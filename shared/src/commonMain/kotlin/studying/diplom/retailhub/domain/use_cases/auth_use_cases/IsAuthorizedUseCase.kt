package studying.diplom.retailhub.domain.use_cases.auth_use_cases

import studying.diplom.retailhub.domain.repositories.AuthRepository

class IsAuthorizedUseCase(private val repository: AuthRepository) {
    operator fun invoke(): Boolean = repository.isAuthorized()
}
