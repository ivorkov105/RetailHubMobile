package studying.diplom.retailhub.domain.use_cases.notifications_use_cases

import studying.diplom.retailhub.domain.repositories.NotificationRepository

class MarkNotificationReadUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: String): Result<Unit> = repository.markAsRead(notificationId)
}
