package studying.diplom.retailhub.data.data_sources

import app.cash.sqldelight.db.SqlDriver
import studying.diplom.retailhub.database.RetailHubDatabase
import studying.diplom.retailhub.domain.models.RequestModel
import studying.diplom.retailhub.data.enteties.request.RequestStatus
import studying.diplom.retailhub.database.RequestEntity

class LocalSource(database: RetailHubDatabase) {
    private val queries = database.retailHubDatabaseQueries

    fun getRequests(): List<RequestModel> {
        return queries.getRequests().executeAsList().map { entity ->
            RequestModel(
                id = entity.id,
                clientSessionToken = entity.clientSessionToken,
                status = RequestStatus.valueOf(entity.status),
                departmentName = entity.departmentName,
                consultantName = entity.consultantName,
                canRemind = entity.canRemind,
                canReassign = entity.canReassign,
                createdAt = entity.createdAt,
                assignedAt = entity.assignedAt
            )
        }
    }

    fun addRequests(newRequests: List<RequestModel>) {
        queries.transaction {
            newRequests.forEach { request ->
                queries.insertRequest(
                    RequestEntity(
                        id = request.id,
                        clientSessionToken = request.clientSessionToken,
                        status = request.status.name,
                        departmentName = request.departmentName,
                        consultantName = request.consultantName,
                        canRemind = request.canRemind,
                        canReassign = request.canReassign,
                        createdAt = request.createdAt,
                        assignedAt = request.assignedAt
                    )
                )
            }
        }
    }

    fun updateRequests(newRequests: List<RequestModel>) {
        queries.transaction {
            queries.removeAllRequests()
            newRequests.forEach { request ->
                queries.insertRequest(
                    RequestEntity(
                        id = request.id,
                        clientSessionToken = request.clientSessionToken,
                        status = request.status.name,
                        departmentName = request.departmentName,
                        consultantName = request.consultantName,
                        canRemind = request.canRemind,
                        canReassign = request.canReassign,
                        createdAt = request.createdAt,
                        assignedAt = request.assignedAt
                    )
                )
            }
        }
    }
}

interface DatabaseDriverFactory {
	fun createDriver(): SqlDriver
}
