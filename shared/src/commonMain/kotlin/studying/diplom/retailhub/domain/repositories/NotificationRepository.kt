package studying.diplom.retailhub.domain.repositories

import kotlinx.coroutines.flow.Flow
import studying.diplom.retailhub.domain.models.notifications.NotificationModel

interface NotificationRepository {
    fun getNotifications(): Flow<List<NotificationModel>>
    suspend fun refreshNotifications(): Result<Unit>
    suspend fun markAsRead(notificationId: String): Result<Unit>
    suspend fun saveNotification(notification: NotificationModel)
}
