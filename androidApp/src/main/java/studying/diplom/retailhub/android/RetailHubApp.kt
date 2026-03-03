package studying.diplom.retailhub.android

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import studying.diplom.retailhub.di.initKoin

class RetailHubApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@RetailHubApp)
        }
    }
}
