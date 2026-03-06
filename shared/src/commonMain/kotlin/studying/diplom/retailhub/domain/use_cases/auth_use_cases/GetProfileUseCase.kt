package studying.diplom.retailhub.domain.use_cases.auth_use_cases

import studying.diplom.retailhub.domain.models.user.UserModel
import studying.diplom.retailhub.domain.repositories.AuthRepository

class GetProfileUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Result<UserModel> = repository.getProfile()
}
