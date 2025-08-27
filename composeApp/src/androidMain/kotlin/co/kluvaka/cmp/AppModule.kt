package co.kluvaka.cmp

import co.kluvaka.cmp.database.AndroidDatabaseDriverFactory
import co.kluvaka.cmp.equipments.data.EquipmentRepositoryImpl
import co.kluvaka.cmp.equipments.domain.EquipmentRepository
import co.kluvaka.cmp.equipments.domain.GetAllEquipments
import co.kluvaka.cmp.equipments.domain.GetAllEquipmentsImpl
import co.kluvaka.cmp.equipments.ui.EquipmentsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

  single<EquipmentRepository> {
    EquipmentRepositoryImpl(
      databaseDriverFactory = AndroidDatabaseDriverFactory(androidContext()),
    )
  }

  single<GetAllEquipments> {
    GetAllEquipmentsImpl(
      repository = get(),
    )
  }

  viewModel {
    EquipmentsViewModel(
      getAllEquipments = get(),
    )
  }
}