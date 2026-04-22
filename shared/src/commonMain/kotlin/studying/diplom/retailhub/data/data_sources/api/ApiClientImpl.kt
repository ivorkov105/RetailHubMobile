package studying.diplom.retailhub.data.data_sources.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import studying.diplom.retailhub.data.enteties.analytics.AnalyticsDashboardEntity
import studying.diplom.retailhub.data.enteties.analytics.ConsultantDetailStatsEntity
import studying.diplom.retailhub.data.enteties.analytics.ConsultantStatsEntity
import studying.diplom.retailhub.data.enteties.auth.LoginRequestEntity
import studying.diplom.retailhub.data.enteties.auth.RefreshTokenRequest
import studying.diplom.retailhub.data.enteties.auth.TokenEntity
import studying.diplom.retailhub.data.enteties.devices.DeviceEntity
import studying.diplom.retailhub.data.enteties.notifications.NotificationEntity
import studying.diplom.retailhub.data.enteties.notifications.NotificationListEntity
import studying.diplom.retailhub.data.enteties.qr_codes.QREntity
import studying.diplom.retailhub.data.enteties.request.RequestEntity
import studying.diplom.retailhub.data.enteties.request.RequestListEntity
import studying.diplom.retailhub.data.enteties.shift.ShiftEntity
import studying.diplom.retailhub.data.enteties.shop.DepartmentEntity
import studying.diplom.retailhub.data.enteties.shop.StoreEntity
import studying.diplom.retailhub.data.enteties.user.UserEntity
import studying.diplom.retailhub.data.enteties.user.UserListEntity

@Serializable
data class ErrorResponse(
    val error: String,
    val message: String,
    val timestamp: String
)

class ApiException(
    val statusCode: HttpStatusCode,
    override val message: String
) : Exception(message)

class ApiClientImpl(
	private val httpClient: HttpClient
) : ApiClient {

    private val json = Json { ignoreUnknownKeys = true }

    private suspend fun HttpResponse.handleResponse(): HttpResponse {
        if (status == HttpStatusCode.OK || status == HttpStatusCode.Created || status == HttpStatusCode.NoContent) {
            return this
        }
        val errorBody = bodyAsText()
        val errorMessage = try {
            json.decodeFromString<ErrorResponse>(errorBody).message
        } catch (e: Exception) {
            "Ошибка сервера: ${status.value}"
        }
        throw ApiException(status, errorMessage)
    }

	override suspend fun login(request: LoginRequestEntity): Result<TokenEntity> = runCatching {
		httpClient.post("auth/login") {
			contentType(ContentType.Application.Json)
			setBody(request)
		}.handleResponse().body()
	}

	override suspend fun refreshToken(refreshToken: String): Result<TokenEntity> = runCatching {
		httpClient.post("auth/refresh") {
			contentType(ContentType.Application.Json)
			setBody(RefreshTokenRequest(refreshToken))
		}.handleResponse().body()
	}

	override suspend fun getMe(): Result<UserEntity> = runCatching {
		httpClient.get("users/me").handleResponse().body()
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
		}.handleResponse().body()
	}

	override suspend fun addRequests(newRequests: List<RequestEntity>): Result<Unit> = runCatching {
		httpClient.post("requests") {
			contentType(ContentType.Application.Json)
			setBody(newRequests)
		}.handleResponse()
	}

	override suspend fun assignRequest(requestId: String): Result<RequestEntity> = runCatching {
		httpClient.post("requests/$requestId/assign").handleResponse().body()
	}

	override suspend fun completeRequest(requestId: String): Result<RequestEntity> = runCatching {
		httpClient.post("requests/$requestId/complete").handleResponse().body()
	}

	override suspend fun getEmployeesAtShift(): Result<List<ShiftEntity>> = runCatching {
		httpClient.get("shifts/active").handleResponse().body()
	}

	override suspend fun startShift(): Result<ShiftEntity> = runCatching {
		httpClient.post("shifts/start").handleResponse().body()
	}

	override suspend fun getMyShifts(
		dateFrom: String,
		dateTo: String
	): Result<List<ShiftEntity>> = runCatching {
		httpClient.get("shifts/my") {
			if (dateFrom.isNotBlank()) parameter("dateFrom", dateFrom)
			if (dateTo.isNotBlank()) parameter("dateTo", dateTo)
		}.handleResponse().body()
	}

	override suspend fun endShift(): Result<ShiftEntity> = runCatching {
		httpClient.post("shifts/end").handleResponse().body()
	}

	override suspend fun addStore(newStore: StoreEntity): Result<Unit> = runCatching {
		httpClient.post("stores") {
			contentType(ContentType.Application.Json)
			setBody(newStore)
		}.handleResponse()
	}

	override suspend fun getMyStore(): Result<StoreEntity> = runCatching {
		httpClient.get("stores/my").handleResponse().body()
	}

	override suspend fun updateMyStore(updatingStore: StoreEntity): Result<StoreEntity> = runCatching {
		httpClient.put("stores/my") {
			contentType(ContentType.Application.Json)
			setBody(updatingStore)
		}.handleResponse().body()
	}

	override suspend fun addDepartment(newDepartment: DepartmentEntity): Result<Unit> = runCatching {
		httpClient.post("stores/my/departments") {
			contentType(ContentType.Application.Json)
			setBody(newDepartment)
		}.handleResponse()
	}

	override suspend fun getMyStoreDepartments(): Result<List<DepartmentEntity>> = runCatching {
		httpClient.get("stores/my/departments").handleResponse().body()
	}

	override suspend fun getDepartment(id: String): Result<DepartmentEntity> = runCatching {
		httpClient.get("departments/${id}") {
			contentType(ContentType.Application.Json)
		}.handleResponse().body()
	}

	override suspend fun updateDepartment(updatingDepartment: DepartmentEntity): Result<DepartmentEntity> = runCatching {
		httpClient.put("departments/${updatingDepartment.id}") {
			contentType(ContentType.Application.Json)
			setBody(updatingDepartment)
		}.handleResponse().body()
	}

	override suspend fun deleteDepartment(deletingDepartment: DepartmentEntity): Result<Unit> = runCatching {
		httpClient.delete("departments/${deletingDepartment.id}") {
			contentType(ContentType.Application.Json)
		}.handleResponse()
	}

	override suspend fun getStoreUsers(): Result<List<UserEntity>> = runCatching {
		httpClient.get("users").handleResponse().body<UserListEntity>().content
	}

	override suspend fun getUser(id: String): Result<UserEntity> = runCatching {
		httpClient.get("users/$id").handleResponse().body()
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
		}.handleResponse()
	}

	override suspend fun updateUser(updatingUser: UserEntity): Result<UserEntity> = runCatching {
		val jsonBody = buildJsonObject {
			put("first_name", updatingUser.firstName)
			put("last_name", updatingUser.lastName)
		}
		httpClient.put("users/${updatingUser.id}") {
			contentType(ContentType.Application.Json)
			setBody(jsonBody)
		}.handleResponse().body()
	}

	override suspend fun deleteUser(deletingUser: UserEntity): Result<Unit> = runCatching {
		httpClient.delete("users/${deletingUser.id}").handleResponse()
	}

	override suspend fun getQrCodes(departmentId: String): Result<List<QREntity>> = runCatching {
		httpClient.get("qr-codes") {
			parameter("departmentId", departmentId)
		}.handleResponse().body()
	}

	override suspend fun postQrCode(
		departmentId: String,
		label: String
	): Result<QREntity> = runCatching {
		httpClient.post("qr-codes") {
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("department_id", departmentId)
                put("label", label)
            })
		}.handleResponse().body()
	}

	override suspend fun downloadQrCode(qrCodeId: String): Result<ByteArray> = runCatching {
		httpClient.get("qr-codes/$qrCodeId").handleResponse().body()
	}

	override suspend fun deleteQrCode(qrCodeId: String): Result<Unit> = runCatching {
		httpClient.delete("qr-codes/$qrCodeId").handleResponse()
	}

    override suspend fun getNotifications(): Result<NotificationListEntity> = runCatching {
        httpClient.get("notifications").handleResponse().body()
    }

    override suspend fun markNotificationAsRead(notificationId: String): Result<Unit> = runCatching {
        httpClient.post("notifications/$notificationId/read").handleResponse()
    }

    override suspend fun registerDevice(device: DeviceEntity): Result<Unit> = runCatching {
        httpClient.post("devices") {
            contentType(ContentType.Application.Json)
            setBody(device)
        }.handleResponse()
    }

    override suspend fun deleteDevice(deviceId: String): Result<Unit> = runCatching {
        httpClient.delete("devices/$deviceId").handleResponse()
    }

    override suspend fun getAnalyticsDashboard(): Result<AnalyticsDashboardEntity> = runCatching {
        httpClient.get("analytics/dashboard").handleResponse().body()
    }

    override suspend fun getConsultantsStats(): Result<List<ConsultantStatsEntity>> = runCatching {
        httpClient.get("analytics/consultants").handleResponse().body()
    }

    override suspend fun getConsultantDetailStats(userId: String): Result<ConsultantDetailStatsEntity> = runCatching {
        httpClient.get("analytics/consultants/$userId").handleResponse().body()
    }

    override suspend fun getRequestsHistory(
        dateFrom: String,
        dateTo: String,
        page: Int,
        size: Int
    ): Result<RequestListEntity> = runCatching {
        httpClient.get("analytics/requests") {
            parameter("dateFrom", dateFrom)
            parameter("dateTo", dateTo)
            parameter("page", page)
            parameter("size", size)
        }.handleResponse().body()
    }
}
