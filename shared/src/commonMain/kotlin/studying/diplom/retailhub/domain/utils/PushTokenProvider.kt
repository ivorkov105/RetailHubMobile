package studying.diplom.retailhub.domain.utils

interface PushTokenProvider {
    suspend fun getPushToken(): String?
    fun getDeviceInfo(): String
}
