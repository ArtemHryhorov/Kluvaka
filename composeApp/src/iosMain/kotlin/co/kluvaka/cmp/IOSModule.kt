package co.kluvaka.cmp

import co.kluvaka.cmp.database.DatabaseDriverFactory
import co.kluvaka.cmp.database.IOSDatabaseDriverFactory
import org.koin.dsl.module

val iosModule = module {
  single<DatabaseDriverFactory> { IOSDatabaseDriverFactory() }
}