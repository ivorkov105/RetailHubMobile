package studying.diplom.retailhub.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import studying.diplom.retailhub.data.data_sources.DatabaseDriverFactory
import studying.diplom.retailhub.database.RetailHubDatabase

class AndroidDatabaseDriverFactory(private val context: Context) : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(RetailHubDatabase.Schema, context, "RetailHubDatabase.db")
    }
}
