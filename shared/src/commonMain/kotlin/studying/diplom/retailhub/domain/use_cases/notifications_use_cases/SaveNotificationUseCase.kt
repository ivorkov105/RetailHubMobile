package studying.diplom.retailhub.domain.use_cases.notifications_use_cases

import studying.diplom.retailhub.domain.models.notifications.NotificationModel
import studying.diplom.retailhub.domain.repositories.NotificationRepository

class SaveNotificationUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notification: NotificationModel) = repository.saveNotification(notification)
}
