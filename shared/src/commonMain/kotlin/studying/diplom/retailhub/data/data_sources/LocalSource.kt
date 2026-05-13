package studying.diplom.retailhub.data.data_sources

import androidx.paging.PagingSource
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import studying.diplom.retailhub.database.RetailHubDatabase
import studying.diplom.retailhub.database.RequestEntity
import studying.diplom.retailhub.database.StoreEntity
import studying.diplom.retailhub.database.DepartmentEntity
import studying.diplom.retailhub.database.UserEntity
import studying.diplom.retailhub.database.SessionEntity
import studying.diplom.retailhub.database.NotificationEntity
import studying.diplom.retailhub.database.DeviceEntity
import studying.diplom.retailhub.database.RequestRemoteKeys

class LocalSource(private val database: RetailHubDatabase) {
    private val queries = database.retailHubDatabaseQueries

    val requestChanges = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    fun <T> withTransaction(block: () -> T): T {
        return database.retailHubDatabaseQueries.transactionWithResult {
            block()
        }
    }

    // Requests
    fun getRequests(
        status: String = "",
        departmentId: String = "",
        dateFrom: String = "",
        dateTo: String = "",
        limit: Long = 20,
        offset: Long = 0
    ): List<RequestEntity> {
        return queries.getRequests(
            status = status,
            departmentId = departmentId,
            dateFrom = dateFrom,
            dateTo = dateTo,
            limit = limit,
            offset = offset
        ).executeAsList()
    }

    fun getRequestsPaged(
        status: String = "",
        departmentId: String = "",
        dateFrom: String = "",
        dateTo: String = ""
    ): PagingSource<Int, RequestEntity> {
        return QueryPagingSource(
            countQuery = queries.countRequests(
                status = status,
                departmentId = departmentId,
                dateFrom = dateFrom,
                dateTo = dateTo
            ),
            transacter = queries,
            context = Dispatchers.IO,
            queryProvider = { limit, offset ->
                queries.getRequests(
                    status = status,
                    departmentId = departmentId,
                    dateFrom = dateFrom,
                    dateTo = dateTo,
                    limit = limit,
                    offset = offset
                )
            }
        ) as PagingSource<Int, RequestEntity>
    }

    fun countRequests(
        status: String = "",
        departmentId: String = "",
        dateFrom: String = "",
        dateTo: String = ""
    ): Long {
        return queries.countRequests(
            status = status,
            departmentId = departmentId,
            dateFrom = dateFrom,
            dateTo = dateTo
        ).executeAsOne()
    }

    fun addRequests(newRequests: List<RequestEntity>) {
        queries.transaction {
            newRequests.forEach { request ->
                queries.insertRequest(request)
            }
        }
        requestChanges.tryEmit(Unit)
    }

    fun clearRequests() {
        queries.removeAllRequests()
        requestChanges.tryEmit(Unit)
    }

    //Remote Keys
    fun getRemoteKey(id: String): RequestRemoteKeys? {
        return queries.getRemoteKey(id).executeAsOneOrNull()
    }

    fun insertRemoteKeys(remoteKeys: List<RequestRemoteKeys>) {
        queries.transaction {
            remoteKeys.forEach { key ->
                queries.insertRemoteKeys(key)
            }
        }
    }

    fun clearRemoteKeys() = queries.clearRemoteKeys()

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

    fun getNotificationsFlow(): Flow<List<NotificationEntity>> {
        return queries.getNotifications().asFlow().mapToList(Dispatchers.Default)
    }

    fun saveNotifications(notifications: List<NotificationEntity>) {
        queries.transaction {
            queries.removeAllNotifications()
            notifications.forEach { queries.insertNotification(it) }
        }
    }

    fun saveNotification(notification: NotificationEntity) {
        queries.insertNotification(notification)
    }

    fun markNotificationAsRead(id: String) {
        queries.markNotificationAsRead(id)
    }

    fun clearAll() {
        queries.transaction {
            queries.removeAllRequests()
            queries.clearRemoteKeys()
            queries.removeStore()
            queries.removeAllDepartments()
            queries.removeAllUsers()
            queries.removeAllQrCodes()
            queries.removeAllNotifications()
            queries.removeSession()
            queries.removeDevice()
        }
        requestChanges.tryEmit(Unit)
    }

    fun clearAllExceptSession() {
        queries.transaction {
            queries.removeAllRequests()
            queries.clearRemoteKeys()
            queries.removeStore()
            queries.removeAllDepartments()
            queries.removeAllUsers()
            queries.removeAllQrCodes()
            queries.removeAllNotifications()
        }
        requestChanges.tryEmit(Unit)
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

    // Device
    fun getDevice(): DeviceEntity? = queries.getDevice().executeAsOneOrNull()

    fun saveDevice(id: String, fcmToken: String) = queries.saveDevice(id, fcmToken)

    fun clearDevice() = queries.removeDevice()
}

interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
