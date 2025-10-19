package co.kluvaka.cmp

import android.app.Application
import co.kluvaka.cmp.di.androidModule
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
      val featureModules = listOf(
        sessionModule,
        equipmentModule,
        trophyModule
      )
      modules(featureModules + androidModule)
    }
  }
}