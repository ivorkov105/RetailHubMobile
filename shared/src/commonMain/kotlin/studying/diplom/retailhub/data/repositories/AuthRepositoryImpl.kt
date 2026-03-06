package studying.diplom.retailhub.data.repositories

import com.russhwolf.settings.Settings
import studying.diplom.retailhub.data.data_sources.api.ApiClient
import studying.diplom.retailhub.data.enteties.auth.LoginRequestEntity
import studying.diplom.retailhub.data.enteties.auth.TokenEntity
import studying.diplom.retailhub.data.mappers.toModel
import studying.diplom.retailhub.domain.models.auth.TokenModel
import studying.diplom.retailhub.domain.models.user.UserModel
import studying.diplom.retailhub.domain.repositories.AuthRepository

class AuthRepositoryImpl(
    private val apiClient: ApiClient,
    private val settings: Settings
) : AuthRepository {

    override suspend fun login(phoneNumber: String, password: String): Result<TokenModel> {
        val result = apiClient.login(LoginRequestEntity(phoneNumber, password))
        return result.map { entity ->
            saveTokens(entity)
            entity.toModel()
        }
    }

    override suspend fun refreshToken(): Result<TokenModel> {
        val currentRefreshToken = settings.getString(KEY_REFRESH_TOKEN, "")
        val result = apiClient.refreshToken(currentRefreshToken)
        return result.map { entity ->
            saveTokens(entity)
            entity.toModel()
        }
    }

    override suspend fun logout() {
        settings.remove(KEY_ACCESS_TOKEN)
        settings.remove(KEY_REFRESH_TOKEN)
    }

    override suspend fun getProfile(): Result<UserModel> {
        return apiClient.getMe().map { it.toModel() }
    }

    override fun isAuthorized(): Boolean {
        return settings.getString(KEY_ACCESS_TOKEN, "").isNotEmpty()
    }

    private fun saveTokens(tokenEntity: TokenEntity) {
        settings.putString(KEY_ACCESS_TOKEN, tokenEntity.accessToken)
        settings.putString(KEY_REFRESH_TOKEN, tokenEntity.refreshToken)
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }
}
