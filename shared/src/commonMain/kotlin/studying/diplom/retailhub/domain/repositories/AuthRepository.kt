package studying.diplom.retailhub.domain.repositories

import studying.diplom.retailhub.domain.models.auth.TokenModel
import studying.diplom.retailhub.domain.models.user.UserModel

interface AuthRepository {
    suspend fun login(phoneNumber: String, password: String): Result<TokenModel>
    suspend fun refreshToken(): Result<TokenModel>
    suspend fun logout()
    suspend fun getProfile(): Result<UserModel>
    fun isAuthorized(): Boolean
}
