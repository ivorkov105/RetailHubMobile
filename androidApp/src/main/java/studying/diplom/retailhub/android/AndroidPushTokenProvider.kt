package studying.diplom.retailhub.android

import android.os.Build
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import studying.diplom.retailhub.domain.utils.PushTokenProvider

class AndroidPushTokenProvider : PushTokenProvider {
    override suspend fun getPushToken(): String? {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            null
        }
    }

    override fun getDeviceInfo(): String {
        return "${Build.MANUFACTURER} ${Build.MODEL} (Android ${Build.VERSION.RELEASE})"
    }
}
