package studying.diplom.retailhub.data.data_sources

import app.cash.sqldelight.db.SqlDriver
import studying.diplom.retailhub.database.RetailHubDatabase
import studying.diplom.retailhub.database.RequestEntity
import studying.diplom.retailhub.database.StoreEntity
import studying.diplom.retailhub.database.DepartmentEntity

class LocalSource(database: RetailHubDatabase) {
    private val queries = database.retailHubDatabaseQueries

    fun getRequests(): List<RequestEntity> {
        return queries.getRequests().executeAsList()
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

    fun getStore(): StoreEntity? {
        return queries.getStore().executeAsOneOrNull()
    }

    fun saveStore(store: StoreEntity) {
        queries.transaction {
            queries.removeStore()
            queries.insertStore(store)
        }
    }

    fun getDepartments(): List<DepartmentEntity> {
        return queries.getDepartments().executeAsList()
    }

    fun saveDepartments(departments: List<DepartmentEntity>) {
        queries.transaction {
            queries.removeAllDepartments()
            departments.forEach { department ->
                queries.insertDepartment(department)
            }
        }
    }

    fun deleteDepartment(id: String) {
        queries.removeDepartment(id)
    }
}

interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
