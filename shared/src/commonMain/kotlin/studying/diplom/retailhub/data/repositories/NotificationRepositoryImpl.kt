package studying.diplom.retailhub.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import studying.diplom.retailhub.data.data_sources.LocalSource
import studying.diplom.retailhub.data.data_sources.RemoteSource
import studying.diplom.retailhub.data.mappers.toModel
import studying.diplom.retailhub.domain.models.notifications.NotificationModel
import studying.diplom.retailhub.domain.repositories.NotificationRepository
import studying.diplom.retailhub.data.mappers.toDbEntity

class NotificationRepositoryImpl(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource
) : NotificationRepository {

    override fun getNotifications(): Flow<List<NotificationModel>> {
        return localSource.getNotificationsFlow().map { list ->
            list.map { it.toModel() }
        }
    }

    override suspend fun refreshNotifications(): Result<Unit> {
        return remoteSource.getNotifications().map { entityList ->
            localSource.saveNotifications(entityList.content.map { it.toDbEntity() })
        }
    }

    override suspend fun markAsRead(notificationId: String): Result<Unit> {
        return remoteSource.markNotificationAsRead(notificationId).onSuccess {
            localSource.markNotificationAsRead(notificationId)
        }
    }

    override suspend fun saveNotification(notification: NotificationModel) {
        localSource.saveNotification(
            studying.diplom.retailhub.database.NotificationEntity(
                id = notification.id,
                title = notification.title,
                body = notification.body,
                isRead = notification.isRead,
                createdAt = notification.createdAt
            )
        )
    }
}
