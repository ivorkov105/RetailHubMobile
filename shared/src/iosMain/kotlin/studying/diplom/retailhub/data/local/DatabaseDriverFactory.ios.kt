package studying.diplom.retailhub.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import studying.diplom.retailhub.data.data_sources.DatabaseDriverFactory
import studying.diplom.retailhub.database.RetailHubDatabase

class IosDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(RetailHubDatabase.Schema, "RetailHubDatabase.db")
    }
}
