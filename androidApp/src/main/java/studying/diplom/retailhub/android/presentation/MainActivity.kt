package studying.diplom.retailhub.android.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import studying.diplom.retailhub.App
import studying.diplom.retailhub.domain.models.devices.DeviceModel
import studying.diplom.retailhub.domain.use_cases.notifications_use_cases.RegisterDeviceUseCase
import studying.diplom.retailhub.domain.utils.PushTokenProvider

class MainActivity : ComponentActivity() {

    private val registerDeviceUseCase: RegisterDeviceUseCase by inject()
    private val pushTokenProvider: PushTokenProvider by inject()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            registerFcmToken()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askNotificationPermission()
        registerFcmToken()

	    handleNotificationIntent(intent)

        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleNotificationIntent(intent: Intent?) {
        val title = intent?.getStringExtra("title")
        val body = intent?.getStringExtra("body")
    }

    private fun registerFcmToken() {
        lifecycleScope.launch {
            val token = pushTokenProvider.getPushToken()
            if (token != null) {
	            Log.d("FCM_TEST", "Token: $token")
                registerDeviceUseCase(
                    DeviceModel(
                        fcmToken = token,
                        deviceInfo = pushTokenProvider.getDeviceInfo()
                    )
                )
            }
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
