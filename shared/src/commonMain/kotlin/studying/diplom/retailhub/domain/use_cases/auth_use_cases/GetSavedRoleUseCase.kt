package studying.diplom.retailhub.domain.use_cases.auth_use_cases

import studying.diplom.retailhub.domain.repositories.AuthRepository

class GetSavedRoleUseCase(private val repository: AuthRepository) {
    operator fun invoke(): String? = repository.getSavedRole()
}
