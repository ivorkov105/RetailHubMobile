package studying.diplom.retailhub.domain.use_cases.user_use_cases

import studying.diplom.retailhub.domain.models.user.UserModel
import studying.diplom.retailhub.domain.repositories.UserRepository

class GetUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(id: String): Result<UserModel> = repository.getUser(id)
}
