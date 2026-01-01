package co.kluvaka.cmp.di

import co.kluvaka.cmp.database.AndroidDatabaseDriverFactory
import co.kluvaka.cmp.database.DatabaseDriverFactory
import co.kluvaka.cmp.features.auth.data.repository.AuthRepositoryImpl
import co.kluvaka.cmp.features.auth.domain.repository.AuthRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidModule = module {
  single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(androidContext()) }
  single<AuthRepository> { AuthRepositoryImpl() }
}
