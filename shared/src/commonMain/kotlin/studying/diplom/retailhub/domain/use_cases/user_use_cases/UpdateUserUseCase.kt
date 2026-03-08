package studying.diplom.retailhub.domain.use_cases.user_use_cases

import studying.diplom.retailhub.domain.models.user.UserModel
import studying.diplom.retailhub.domain.repositories.UserRepository

class UpdateUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(user: UserModel): Result<UserModel> = repository.updateUser(user)
}
