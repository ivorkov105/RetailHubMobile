package studying.diplom.retailhub.data.data_sources.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import studying.diplom.retailhub.data.enteties.auth.LoginRequestEntity
import studying.diplom.retailhub.data.enteties.auth.RefreshTokenRequest
import studying.diplom.retailhub.data.enteties.auth.TokenEntity
import studying.diplom.retailhub.data.enteties.request.RequestEntity
import studying.diplom.retailhub.data.enteties.user.UserEntity

class ApiClientImpl(
    private val httpClient: HttpClient
) : ApiClient {

    override suspend fun login(request: LoginRequestEntity): Result<TokenEntity> = runCatching {
        httpClient.post("auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun refreshToken(refreshToken: String): Result<TokenEntity> = runCatching {
        httpClient.post("auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(RefreshTokenRequest(refreshToken))
        }.body()
    }

    override suspend fun getMe(): Result<UserEntity> = runCatching {
        httpClient.get("auth/me").body()
    }

    override suspend fun getRequests(): Result<List<RequestEntity>> = runCatching {
        httpClient.get("requests").body()
    }

    override suspend fun addRequests(newRequests: List<RequestEntity>): Result<Unit> = runCatching {
        httpClient.post("requests") {
            contentType(ContentType.Application.Json)
            setBody(newRequests)
        }
        Unit
    }
}
