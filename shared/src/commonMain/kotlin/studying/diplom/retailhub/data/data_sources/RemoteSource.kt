package studying.diplom.retailhub.data.data_sources

import studying.diplom.retailhub.data.data_sources.api.ApiClient
import studying.diplom.retailhub.data.enteties.auth.LoginRequestEntity
import studying.diplom.retailhub.data.enteties.auth.TokenEntity
import studying.diplom.retailhub.data.enteties.request.RequestEntity
import studying.diplom.retailhub.data.enteties.request.RequestListEntity
import studying.diplom.retailhub.data.enteties.shift.ShiftEntity
import studying.diplom.retailhub.data.enteties.shop.DepartmentEntity
import studying.diplom.retailhub.data.enteties.shop.StoreEntity
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

    override suspend fun getRequests(
        status: String,
        departmentId: String,
        dateFrom: String,
        dateTo: String,
        page: Int,
        size: Int
    ): Result<RequestListEntity> {
        return apiClient.getRequests(status, departmentId, dateFrom, dateTo, page, size)
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

	override suspend fun startShift(): Result<ShiftEntity> {
		return apiClient.startShift()
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

	override suspend fun deleteUser(deletingUser: UserEntity): Result<Unit> {
		return apiClient.deleteUser(deletingUser)
	}
}
