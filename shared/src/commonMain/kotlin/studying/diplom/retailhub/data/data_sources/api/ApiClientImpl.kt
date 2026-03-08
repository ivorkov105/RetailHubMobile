package studying.diplom.retailhub.data.data_sources.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import studying.diplom.retailhub.data.enteties.auth.LoginRequestEntity
import studying.diplom.retailhub.data.enteties.auth.RefreshTokenRequest
import studying.diplom.retailhub.data.enteties.auth.TokenEntity
import studying.diplom.retailhub.data.enteties.request.RequestEntity
import studying.diplom.retailhub.data.enteties.request.RequestListEntity
import studying.diplom.retailhub.data.enteties.shift.ShiftEntity
import studying.diplom.retailhub.data.enteties.shop.DepartmentEntity
import studying.diplom.retailhub.data.enteties.shop.StoreEntity
import studying.diplom.retailhub.data.enteties.user.UserEntity
import studying.diplom.retailhub.data.enteties.user.UserListEntity

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

	override suspend fun getRequests(
		status: String,
		departmentId: String,
		dateFrom: String,
		dateTo: String,
		page: Int,
		size: Int
	): Result<RequestListEntity> = runCatching {
		httpClient.get("requests") {
			if (status.isNotBlank()) parameter("status", status)
			if (departmentId.isNotBlank()) parameter("departmentId", departmentId)
			if (dateFrom.isNotBlank()) parameter("dateFrom", dateFrom)
			if (dateTo.isNotBlank()) parameter("dateTo", dateTo)
			parameter("page", page)
			parameter("size", size)
		}.body()
	}

	override suspend fun addRequests(newRequests: List<RequestEntity>): Result<Unit> = runCatching {
		httpClient.post("requests") {
			contentType(ContentType.Application.Json)
			setBody(newRequests)
		}
	}

	override suspend fun assignRequest(requestId: String): Result<RequestEntity> = runCatching {
		httpClient.post("requests/$requestId/assign").body()
	}

	override suspend fun completeRequest(requestId: String): Result<RequestEntity> = runCatching {
		httpClient.post("requests/$requestId/complete").body()
	}

	override suspend fun startShift(): Result<ShiftEntity> = runCatching {
		httpClient.post("shifts/start").body()
	}

	override suspend fun endShift(): Result<ShiftEntity> = runCatching {
		httpClient.post("shifts/end").body()
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

	override suspend fun getDepartment(id: String): Result<DepartmentEntity> = runCatching {
		httpClient.get("departments/${id}") {
			contentType(ContentType.Application.Json)
		}.body()
	}

	override suspend fun updateDepartment(updatingDepartment: DepartmentEntity): Result<DepartmentEntity> = runCatching {
		httpClient.put("departments/${updatingDepartment.id}") {
			contentType(ContentType.Application.Json)
			setBody(updatingDepartment)
		}.body()
	}

	override suspend fun deleteDepartment(deletingDepartment: DepartmentEntity): Result<Unit> = runCatching {
		httpClient.delete("departments/${deletingDepartment.id}") {
			contentType(ContentType.Application.Json)
		}
	}

	override suspend fun getStoreUsers(): Result<List<UserEntity>> = runCatching {
		httpClient.get("users").body<UserListEntity>().content
	}

	override suspend fun getUser(id: String): Result<UserEntity> = runCatching {
		httpClient.get("users/$id").body()
	}

	override suspend fun addUser(newUser: UserEntity): Result<Unit> = runCatching {
		val jsonBody = buildJsonObject {
			put("phone_number", newUser.phoneNumber)
			put("password", newUser.password)
			put("first_name", newUser.firstName)
			put("last_name", newUser.lastName)
			put("role", newUser.role)
			putJsonArray("department_ids") {
				newUser.departments.forEach { add(JsonPrimitive(it.id)) }
			}
		}
		httpClient.post("users") {
			contentType(ContentType.Application.Json)
			setBody(jsonBody)
		}
	}

	override suspend fun updateUser(updatingUser: UserEntity): Result<UserEntity> = runCatching {
		val jsonBody = buildJsonObject {
			put("first_name", updatingUser.firstName)
			put("last_name", updatingUser.lastName)
		}
		httpClient.put("users/${updatingUser.id}") {
			contentType(ContentType.Application.Json)
			setBody(jsonBody)
		}.body()
	}

	override suspend fun deleteUser(deletingUser: UserEntity): Result<Unit> = runCatching {
		httpClient.delete("users/${deletingUser.id}")
	}
}
