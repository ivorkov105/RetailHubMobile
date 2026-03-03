package studying.diplom.retailhub.di

import com.russhwolf.settings.Settings
import com.russhwolf.settings.NSUserDefaultsSettings
import platform.Foundation.NSUserDefaults
import org.koin.core.module.Module
import org.koin.dsl.module
import studying.diplom.retailhub.data.data_sources.DatabaseDriverFactory
import studying.diplom.retailhub.data.local.IosDatabaseDriverFactory

actual fun platformModule(): Module = module {
    single<DatabaseDriverFactory> { IosDatabaseDriverFactory() }
    single<Settings> { 
        val userDefaults = NSUserDefaults.standardUserDefaults
        NSUserDefaultsSettings(userDefaults)
    }
}
