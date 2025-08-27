package co.kluvaka.cmp

import co.kluvaka.cmp.database.AndroidDatabaseDriverFactory
import co.kluvaka.cmp.database.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidModule = module {
  single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(androidContext()) }
}
