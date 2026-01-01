package co.kluvaka.cmp.di

import co.kluvaka.cmp.database.DatabaseDriverFactory
import co.kluvaka.cmp.database.IOSDatabaseDriverFactory
import co.kluvaka.cmp.features.auth.data.repository.AuthRepositoryImpl
import co.kluvaka.cmp.features.auth.domain.repository.AuthRepository
import org.koin.dsl.module

val iosModule = module {
  single<DatabaseDriverFactory> { IOSDatabaseDriverFactory() }
  
  // Auth Repository - iOS implementation (currently NoOp, replace with Firebase iOS SDK when ready)
  single<AuthRepository> { AuthRepositoryImpl() }
}