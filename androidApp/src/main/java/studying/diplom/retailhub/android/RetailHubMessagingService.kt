package studying.diplom.retailhub.android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import studying.diplom.retailhub.android.presentation.MainActivity
import studying.diplom.retailhub.domain.models.devices.DeviceModel
import studying.diplom.retailhub.domain.models.notifications.NotificationModel
import studying.diplom.retailhub.domain.repositories.AuthRepository
import studying.diplom.retailhub.domain.use_cases.notifications_use_cases.RegisterDeviceUseCase
import studying.diplom.retailhub.domain.use_cases.notifications_use_cases.SaveNotificationUseCase
import java.util.UUID

class RetailHubMessagingService : FirebaseMessagingService() {

    private val registerDeviceUseCase: RegisterDeviceUseCase by inject()
    private val saveNotificationUseCase: SaveNotificationUseCase by inject()
    private val authRepository: AuthRepository by inject()
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        if (authRepository.isAuthorized()) {
            scope.launch {
                registerDeviceUseCase(
                    DeviceModel(
                        fcmToken = token,
                        deviceInfo = "${Build.MANUFACTURER} ${Build.MODEL} (Android ${Build.VERSION.RELEASE})"
                    )
                )
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        
        val title = message.notification?.title ?: message.data["title"]
        val body = message.notification?.body ?: message.data["body"]
        
        if (title != null && body != null) {
            val id = message.messageId ?: UUID.randomUUID().toString()
            // Используем timestamp для удобной сортировки в локальной БД
            val createdAt = System.currentTimeMillis().toString()

            // Сохраняем уведомление в локальную БД через UseCase из shared модуля
            scope.launch {
                saveNotificationUseCase(
                    NotificationModel(
                        id = id,
                        title = title,
                        body = body,
                        isRead = false,
                        createdAt = createdAt
                    )
                )
            }

            sendNotification(title, body, message.data)
        }
    }

    private fun sendNotification(title: String, body: String, data: Map<String, String>) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            // Передаем все данные из пуша в Intent, чтобы MainActivity могла их обработать
            data.forEach { (key, value) ->
                putExtra(key, value)
            }
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "retail_hub_notifications"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Retail Hub Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}
