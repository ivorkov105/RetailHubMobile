package studying.diplom.retailhub.android

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.dsl.module
import studying.diplom.retailhub.di.initKoin
import studying.diplom.retailhub.domain.utils.PushTokenProvider

class RetailHubApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@RetailHubApp)
            modules(module {
                single<PushTokenProvider> { AndroidPushTokenProvider() }
            })
        }
    }
}
