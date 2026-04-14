package studying.diplom.retailhub.domain.use_cases.auth_use_cases

import studying.diplom.retailhub.domain.models.auth.TokenModel
import studying.diplom.retailhub.domain.repositories.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(phoneNumber: String, password: String): Result<TokenModel> {
        return repository.login(phoneNumber, password)
    }
}
