package studying.diplom.retailhub.data.data_sources

import studying.diplom.retailhub.data.data_sources.api.ApiClient
import studying.diplom.retailhub.data.enteties.auth.LoginRequestEntity
import studying.diplom.retailhub.data.enteties.auth.TokenEntity
import studying.diplom.retailhub.data.enteties.request.RequestEntity
import studying.diplom.retailhub.data.enteties.user.UserEntity

class RemoteSource(
    private val apiClient: ApiClient
) : ApiClient {

    override suspend fun login(request: LoginRequestEntity): Result<TokenEntity> {
        return apiClient.login(request)
    }

    override suspend fun refreshToken(refreshToken: String): Result<TokenEntity> {
        return apiClient.refreshToken(refreshToken)
    }

    override suspend fun getMe(): Result<UserEntity> {
        return apiClient.getMe()
    }

    override suspend fun getRequests(): Result<List<RequestEntity>> {
        return apiClient.getRequests()
    }

    override suspend fun addRequests(newRequests: List<RequestEntity>): Result<Unit> {
        return apiClient.addRequests(newRequests)
    }
}
