package studying.diplom.retailhub.domain.repositories

import kotlinx.coroutines.flow.Flow
import studying.diplom.retailhub.domain.models.request.RequestModel

interface RequestRepository {
    suspend fun addRequests(requests: List<RequestModel>): Result<Unit>

    suspend fun assignRequest(requestId: String): Result<RequestModel>

    suspend fun completeRequest(requestId: String): Result<RequestModel>

    // WebSocket methods
    fun observeRequestUpdates(): Flow<RequestModel>
    fun connectToWebSocket()
    fun disconnectFromWebSocket()
    fun subscribeToStore(storeId: String)
    fun subscribeToDepartment(departmentId: String)
    fun unsubscribeFromStore(storeId: String)
    fun unsubscribeFromDepartment(departmentId: String)
}
