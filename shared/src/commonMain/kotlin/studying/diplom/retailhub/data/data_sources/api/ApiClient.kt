package studying.diplom.retailhub.data.data_sources.api

import studying.diplom.retailhub.data.enteties.auth.LoginRequestEntity
import studying.diplom.retailhub.data.enteties.auth.TokenEntity
import studying.diplom.retailhub.data.enteties.request.RequestEntity
import studying.diplom.retailhub.data.enteties.user.UserEntity

interface ApiClient {

	//Авторизация
    suspend fun login(request: LoginRequestEntity): Result<TokenEntity>
    suspend fun refreshToken(refreshToken: String): Result<TokenEntity>
    suspend fun getMe(): Result<UserEntity>

	//Заявки
    suspend fun getRequests(): Result<List<RequestEntity>>
    suspend fun addRequests(newRequests: List<RequestEntity>): Result<Unit>
}
