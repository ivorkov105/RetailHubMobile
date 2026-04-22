package studying.diplom.retailhub.di

import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import android.content.Context
import org.koin.core.module.Module
import org.koin.dsl.module
import studying.diplom.retailhub.data.data_sources.DatabaseDriverFactory
import studying.diplom.retailhub.data.local.AndroidDatabaseDriverFactory
import studying.diplom.retailhub.presentation.main.utils.ImageSaver
import studying.diplom.retailhub.presentation.main.utils.AndroidImageSaver

actual fun platformModule(): Module = module {
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(get()) }
    single<Settings> { 
        val context: Context = get()
        val sharedPrefs = context.getSharedPreferences("retail_hub_settings", Context.MODE_PRIVATE)
        SharedPreferencesSettings(sharedPrefs)
    }
    single<ImageSaver> { AndroidImageSaver(get()) }
}
