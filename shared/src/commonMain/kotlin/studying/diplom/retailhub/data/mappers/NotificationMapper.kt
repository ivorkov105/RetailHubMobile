package studying.diplom.retailhub.data.mappers

import studying.diplom.retailhub.data.entities.notifications.NotificationEntity as ApiNotificationEntity
import studying.diplom.retailhub.database.NotificationEntity as DbNotificationEntity
import studying.diplom.retailhub.domain.models.notifications.NotificationModel

fun ApiNotificationEntity.toModel() = NotificationModel(
    id = id,
    title = title,
    body = body,
    isRead = isRead,
    createdAt = createdAt
)

fun ApiNotificationEntity.toDbEntity() = DbNotificationEntity(
    id = id,
    title = title,
    body = body,
    isRead = isRead,
    createdAt = createdAt
)

fun DbNotificationEntity.toModel() = NotificationModel(
    id = id,
    title = title,
    body = body,
    isRead = isRead,
    createdAt = createdAt
)

fun NotificationModel.toDbEntity() = DbNotificationEntity(
    id = id,
    title = title,
    body = body,
    isRead = isRead,
    createdAt = createdAt
)
