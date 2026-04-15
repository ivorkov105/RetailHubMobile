package studying.diplom.retailhub.domain.use_cases.notifications_use_cases

import kotlinx.coroutines.flow.Flow
import studying.diplom.retailhub.domain.models.notifications.NotificationModel
import studying.diplom.retailhub.domain.repositories.NotificationRepository

class GetNotificationsUseCase(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<List<NotificationModel>> = repository.getNotifications()
}
