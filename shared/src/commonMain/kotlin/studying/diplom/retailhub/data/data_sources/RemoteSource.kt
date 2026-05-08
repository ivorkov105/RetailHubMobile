package studying.diplom.retailhub.data.data_sources

import studying.diplom.retailhub.data.data_sources.api.ApiClient
import studying.diplom.retailhub.data.entities.analytics.AnalyticsDashboardEntity
import studying.diplom.retailhub.data.entities.analytics.ConsultantDetailStatsEntity
import studying.diplom.retailhub.data.entities.analytics.ConsultantStatsEntity
import studying.diplom.retailhub.data.entities.auth.LoginRequestEntity
import studying.diplom.retailhub.data.entities.auth.TokenEntity
import studying.diplom.retailhub.data.entities.devices.registration.DeviceRegistrationRequest
import studying.diplom.retailhub.data.entities.devices.registration.DeviceRegistrationResponse
import studying.diplom.retailhub.data.entities.notifications.NotificationListEntity
import studying.diplom.retailhub.data.entities.qr_codes.QREntity
import studying.diplom.retailhub.data.entities.request.RequestEntity
import studying.diplom.retailhub.data.entities.request.RequestListEntity
import studying.diplom.retailhub.data.entities.shift.ShiftEntity
import studying.diplom.retailhub.data.entities.shop.DepartmentEntity
import studying.diplom.retailhub.data.entities.shop.StoreEntity
import studying.diplom.retailhub.data.entities.user.UserEntity

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

	override fun clearAuthTokens() {
		return apiClient.clearAuthTokens()
	}

	override suspend fun addRequests(newRequests: List<RequestEntity>): Result<Unit> {
        return apiClient.addRequests(newRequests)
    }

    override suspend fun assignRequest(requestId: String): Result<RequestEntity> {
        return apiClient.assignRequest(requestId)
    }

    override suspend fun completeRequest(requestId: String): Result<RequestEntity> {
        return apiClient.completeRequest(requestId)
    }

    override suspend fun getRequests(
        status: String?,
        departmentId: String?,
        dateFrom: String?,
        dateTo: String?,
        page: Int,
        size: Int
    ): Result<RequestListEntity> {
        return apiClient.getRequests(status, departmentId, dateFrom, dateTo, page, size)
    }

	override suspend fun getEmployeesAtShift(): Result<List<ShiftEntity>> {
		return apiClient.getEmployeesAtShift()
	}

	override suspend fun startShift(): Result<ShiftEntity> {
		return apiClient.startShift()
	}

	override suspend fun getMyShifts(
		dateFrom: String,
		dateTo: String
	): Result<List<ShiftEntity>> {
		return apiClient.getMyShifts(dateFrom, dateTo)
	}

	override suspend fun endShift(): Result<ShiftEntity> {
		return apiClient.endShift()
	}

	override suspend fun addStore(newStore: StoreEntity): Result<Unit> {
        return apiClient.addStore(newStore)
    }

    override suspend fun getMyStore(): Result<StoreEntity> {
        return apiClient.getMyStore()
    }

    override suspend fun updateMyStore(updatingStore: StoreEntity): Result<StoreEntity> {
        return apiClient.updateMyStore(updatingStore)
    }

    override suspend fun addDepartment(newDepartment: DepartmentEntity): Result<Unit> {
        return apiClient.addDepartment(newDepartment)
    }

    override suspend fun getMyStoreDepartments(): Result<List<DepartmentEntity>> {
        return apiClient.getMyStoreDepartments()
    }

	override suspend fun getDepartment(id: String): Result<DepartmentEntity> {
		return apiClient.getDepartment(id)
	}

	override suspend fun updateDepartment(updatingDepartment: DepartmentEntity): Result<DepartmentEntity> {
        return apiClient.updateDepartment(updatingDepartment)
    }

    override suspend fun deleteDepartment(deletingDepartment: DepartmentEntity): Result<Unit> {
        return apiClient.deleteDepartment(deletingDepartment)
    }

	override suspend fun getStoreUsers(): Result<List<UserEntity>> {
		return apiClient.getStoreUsers()
	}

	override suspend fun getUser(id: String): Result<UserEntity> {
		return apiClient.getUser(id)
	}

	override suspend fun addUser(newUser: UserEntity): Result<Unit> {
		return apiClient.addUser(newUser)
	}

	override suspend fun updateUser(updatingUser: UserEntity): Result<UserEntity> {
		return apiClient.updateUser(updatingUser)
	}

	override suspend fun updateUserDepartments(
		userId: String,
		departmentIds: List<String>
	): Result<UserEntity> {
		return apiClient.updateUserDepartments(userId, departmentIds)
	}

	override suspend fun deleteUser(deletingUser: UserEntity): Result<Unit> {
		return apiClient.deleteUser(deletingUser)
	}

	override suspend fun getQrCodes(departmentId: String): Result<List<QREntity>> {
		return apiClient.getQrCodes(departmentId)
	}

	override suspend fun postQrCode(
		departmentId: String,
		label: String
	): Result<QREntity> {
		return apiClient.postQrCode(departmentId, label)
	}

	override suspend fun downloadQrCode(qrCodeId: String): Result<ByteArray> {
		return apiClient.downloadQrCode(qrCodeId)
	}

	override suspend fun deleteQrCode(qrCodeId: String): Result<Unit> {
		return apiClient.deleteQrCode(qrCodeId)
	}

    override suspend fun getNotifications(): Result<NotificationListEntity> {
        return apiClient.getNotifications()
    }

    override suspend fun markNotificationAsRead(notificationId: String): Result<Unit> {
        return apiClient.markNotificationAsRead(notificationId)
    }

    override suspend fun registerDevice(device: DeviceRegistrationRequest): Result<DeviceRegistrationResponse> {
        return apiClient.registerDevice(device)
    }

    override suspend fun deleteDevice(deviceId: String): Result<Unit> {
        return apiClient.deleteDevice(deviceId)
    }

    override suspend fun getAnalyticsDashboard(): Result<AnalyticsDashboardEntity> {
        return apiClient.getAnalyticsDashboard()
    }

    override suspend fun getConsultantsStats(
		dateFrom: String,
		dateTo: String
	): Result<List<ConsultantStatsEntity>> {
        return apiClient.getConsultantsStats(dateFrom, dateTo)
    }

    override suspend fun getConsultantDetailStats(
		userId: String,
		dateFrom: String,
		dateTo: String
	): Result<ConsultantDetailStatsEntity> {
        return apiClient.getConsultantDetailStats(userId, dateFrom, dateTo)
    }

    override suspend fun getRequestsHistory(
		status: String?,
		departmentId: String?,
		assignedUserId: String?,
		dateFrom: String?,
		dateTo: String?,
		page: Int,
		size: Int
	): Result<RequestListEntity> {
        return apiClient.getRequestsHistory(status, departmentId, assignedUserId, dateFrom, dateTo, page, size)
    }
}
