package studying.diplom.retailhub.data.data_sources

import app.cash.sqldelight.db.SqlDriver
import studying.diplom.retailhub.database.RetailHubDatabase
import studying.diplom.retailhub.database.RequestEntity
import studying.diplom.retailhub.database.StoreEntity
import studying.diplom.retailhub.database.DepartmentEntity
import studying.diplom.retailhub.database.UserEntity
import studying.diplom.retailhub.database.SessionEntity
import studying.diplom.retailhub.database.NotificationEntity

class LocalSource(database: RetailHubDatabase) {
    private val queries = database.retailHubDatabaseQueries

    // Requests
    fun getRequests(
        status: String = "",
        departmentId: String = "",
        limit: Long = 20,
        offset: Long = 0
    ): List<RequestEntity> {
        return queries.getRequests(
            status = status,
            departmentId = departmentId,
            limit = limit,
            offset = offset
        ).executeAsList()
    }

    fun addRequests(newRequests: List<RequestEntity>) {
        queries.transaction {
            newRequests.forEach { request ->
                queries.insertRequest(request)
            }
        }
    }

    fun updateRequests(newRequests: List<RequestEntity>) {
        queries.transaction {
            queries.removeAllRequests()
            newRequests.forEach { request ->
                queries.insertRequest(request)
            }
        }
    }

    fun clearRequests() = queries.removeAllRequests()

    // Stores
    fun getStore(): StoreEntity? {
        return queries.getStore().executeAsOneOrNull()
    }

    fun saveStore(store: StoreEntity) {
        queries.transaction {
            queries.removeStore()
            queries.insertStore(store)
        }
    }

    fun clearStore() = queries.removeStore()

    // Departments
    fun getDepartments(): List<DepartmentEntity> {
        return queries.getDepartments().executeAsList()
    }

    fun getDepartment(id: String): DepartmentEntity? {
        return queries.getDepartment(id).executeAsOneOrNull()
    }

    fun saveDepartments(departments: List<DepartmentEntity>) {
        queries.transaction {
            queries.removeAllDepartments()
            departments.forEach { department ->
                queries.insertDepartment(department)
            }
        }
    }

    fun saveDepartment(department: DepartmentEntity) {
        queries.insertDepartment(department)
    }

    fun deleteDepartment(id: String) {
        queries.removeDepartment(id)
    }

    fun clearDepartments() = queries.removeAllDepartments()

	// Users
	fun getStoreUsers(): List<UserEntity> = queries.getStoreUsers().executeAsList()

	fun getUser(id: String): UserEntity? = queries.getUser(id).executeAsOneOrNull()

	fun saveUsers(users: List<UserEntity>) {
		queries.transaction {
			queries.removeAllUsers()
			users.forEach { queries.insertUser(it) }
		}
	}

	fun saveUser(user: UserEntity) = queries.insertUser(user)

	fun deleteUser(id: String) = queries.removeUser(id)

    fun clearUsers() = queries.removeAllUsers()

    // Notifications
    fun getNotifications(): List<NotificationEntity> {
        return queries.getNotifications().executeAsList()
    }

    fun saveNotifications(notifications: List<NotificationEntity>) {
        queries.transaction {
            queries.removeAllNotifications()
            notifications.forEach { queries.insertNotification(it) }
        }
    }

    fun markNotificationAsRead(id: String) {
        queries.markNotificationAsRead(id)
    }

    fun clearNotifications() = queries.removeAllNotifications()

    fun clearAll() {
        queries.transaction {
            queries.removeAllRequests()
            queries.removeStore()
            queries.removeAllDepartments()
            queries.removeAllUsers()
            queries.removeAllQrCodes()
            queries.removeAllNotifications()
            queries.removeSession()
        }
    }

    fun clearAllExceptSession() {
        queries.transaction {
            queries.removeAllRequests()
            queries.removeStore()
            queries.removeAllDepartments()
            queries.removeAllUsers()
            queries.removeAllQrCodes()
            queries.removeAllNotifications()
        }
    }

    // Session
    fun getSession(): SessionEntity? {
        return queries.getSession().executeAsOneOrNull()
    }

    fun saveSession(accessToken: String, refreshToken: String, userRole: String?) {
        queries.saveSession(accessToken, refreshToken, userRole)
    }

    fun updateUserRole(userRole: String) {
        queries.updateUserRole(userRole)
    }

    fun clearSession() {
        queries.removeSession()
    }
}

interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
