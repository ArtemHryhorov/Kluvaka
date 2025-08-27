package co.kluvaka.cmp.database

import app.cash.sqldelight.db.SqlDriver

interface DatabaseDriverFactory {
  fun createDriver(): SqlDriver
}