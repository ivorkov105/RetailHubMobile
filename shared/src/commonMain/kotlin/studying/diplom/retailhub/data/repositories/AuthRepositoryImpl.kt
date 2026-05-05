package studying.diplom.retailhub.data.repositories

import studying.diplom.retailhub.data.data_sources.LocalSource
import studying.diplom.retailhub.data.data_sources.RemoteSource
import studying.diplom.retailhub.data.entities.auth.LoginRequestEntity
import studying.diplom.retailhub.data.entities.auth.TokenEntity
import studying.diplom.retailhub.data.mappers.toModel
import studying.diplom.retailhub.domain.models.auth.TokenModel
import studying.diplom.retailhub.domain.models.user.UserModel
import studying.diplom.retailhub.domain.repositories.AuthRepository

class AuthRepositoryImpl(
	private val remoteSource: RemoteSource,
	private val localSource: LocalSource
) : AuthRepository {

    override suspend fun login(phoneNumber: String, password: String): Result<TokenModel> {
        val result = remoteSource.login(LoginRequestEntity(phoneNumber, password))
        return result.map { entity ->
            saveTokens(entity)
            entity.toModel()
        }
    }

    override suspend fun refreshToken(): Result<TokenModel> {
        val currentRefreshToken = localSource.getSession()?.refreshToken ?: ""
        val result = remoteSource.refreshToken(currentRefreshToken)
        return result.map { entity ->
            saveTokens(entity)
            entity.toModel()
        }
    }

    override suspend fun logout() {
        remoteSource.clearAuthTokens()
        localSource.clearAll()
    }

    override suspend fun getProfile(): Result<UserModel> {
        return remoteSource.getMe().map {
            val model = it.toModel()
            localSource.updateUserRole(model.role)
            model
        }
    }

    override fun isAuthorized(): Boolean {
        return localSource.getSession()?.accessToken?.isNotEmpty() ?: false
    }

    override fun saveTokens(tokenEntity: TokenEntity) {
        val currentSession = localSource.getSession()
        localSource.saveSession(
            accessToken = tokenEntity.accessToken,
            refreshToken = tokenEntity.refreshToken,
            userRole = currentSession?.userRole
        )
    }

    override fun getSavedRole(): String? {
        return localSource.getSession()?.userRole
    }
}
