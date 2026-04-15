package studying.diplom.retailhub.domain.use_cases.notifications_use_cases

import studying.diplom.retailhub.domain.repositories.NotificationRepository

class RefreshNotificationsUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): Result<Unit> = repository.refreshNotifications()
}
