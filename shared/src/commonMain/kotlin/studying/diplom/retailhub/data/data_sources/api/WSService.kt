package studying.diplom.retailhub.data.data_sources.api

import kotlinx.coroutines.flow.SharedFlow
import studying.diplom.retailhub.data.entities.request.RequestEntity

interface WSService {
    val requestUpdates: SharedFlow<RequestEntity>
    fun connect()
    fun subscribeToStore(storeId: String)
    fun subscribeToDepartment(departmentId: String)
    fun unsubscribeFromStore(storeId: String)
    fun unsubscribeFromDepartment(departmentId: String)
    fun disconnect()
}
