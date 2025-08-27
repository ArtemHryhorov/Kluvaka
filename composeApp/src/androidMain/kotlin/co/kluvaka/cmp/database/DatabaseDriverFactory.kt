package co.kluvaka.cmp.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

class AndroidDatabaseDriverFactory(private val context: Context) : DatabaseDriverFactory {
  override fun createDriver(): SqlDriver {
    return AndroidSqliteDriver(AppDatabase.Schema, context, "equipment.db")
  }
}