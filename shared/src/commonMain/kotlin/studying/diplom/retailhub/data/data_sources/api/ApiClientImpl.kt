package studying.diplom.retailhub.data.data_sources.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import studying.diplom.retailhub.data.enteties.auth.LoginRequestEntity
import studying.diplom.retailhub.data.enteties.auth.RefreshTokenRequest
import studying.diplom.retailhub.data.enteties.auth.TokenEntity
import studying.diplom.retailhub.data.enteties.request.RequestEntity
import studying.diplom.retailhub.data.enteties.shop.DepartmentEntity
import studying.diplom.retailhub.data.enteties.shop.StoreEntity
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
    }

	override suspend fun addStore(newStore: StoreEntity): Result<Unit> = runCatching {
		httpClient.post("stores") {
			contentType(ContentType.Application.Json)
			setBody(newStore)
		}
	}

	override suspend fun getMyStore(): Result<StoreEntity> = runCatching {
		httpClient.get("stores/my").body()
	}

	override suspend fun updateMyStore(updatingStore: StoreEntity): Result<StoreEntity> = runCatching {
		httpClient.put("stores/my") {
			contentType(ContentType.Application.Json)
			setBody(updatingStore)
		}.body()
	}

	override suspend fun addDepartment(newDepartment: DepartmentEntity): Result<Unit> = runCatching {
		httpClient.post("stores/my/departments") {
			contentType(ContentType.Application.Json)
			setBody(newDepartment)
		}
	}

	override suspend fun getMyStoreDepartments(): Result<List<DepartmentEntity>> = runCatching {
		httpClient.get("stores/my/departments").body()
	}

	override suspend fun updateDepartment(updatingDepartment: DepartmentEntity): Result<DepartmentEntity> = runCatching {
		httpClient.put("stores/my/departments") {
			contentType(ContentType.Application.Json)
			setBody(updatingDepartment)
		}.body()
	}

	override suspend fun deleteDepartment(deletingDepartment: DepartmentEntity): Result<Unit> = runCatching {
		httpClient.delete("stores/my/departments") {
			contentType(ContentType.Application.Json)
			setBody(deletingDepartment)
		}
	}
}
