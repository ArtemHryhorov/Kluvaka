package co.kluvaka.cmp

import android.app.Application
import co.kluvaka.cmp.di.androidModule
import co.kluvaka.cmp.di.authModule
import co.kluvaka.cmp.di.equipmentModule
import co.kluvaka.cmp.di.sessionModule
import co.kluvaka.cmp.di.trophyModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {
  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidContext(this@MainApplication)
      // Load platform-specific module first (provides AuthRepository)
      modules(androidModule)

      // Then load feature modules that depend on platform-specific dependencies
      val featureModules = listOf(
        authModule,
        sessionModule,
        equipmentModule,
        trophyModule
      )
      modules(featureModules)
    }
  }
}