package co.kluvaka.cmp

import co.kluvaka.cmp.database.AndroidDatabaseDriverFactory
import co.kluvaka.cmp.equipment.data.EquipmentRepositoryImpl
import co.kluvaka.cmp.equipment.domain.EquipmentRepository
import co.kluvaka.cmp.equipment.domain.usecase.AddEquipment
import co.kluvaka.cmp.equipment.domain.usecase.AddEquipmentImpl
import co.kluvaka.cmp.equipment.domain.usecase.GetAllEquipments
import co.kluvaka.cmp.equipment.domain.usecase.GetAllEquipmentsImpl
import co.kluvaka.cmp.equipment.ui.add.equipment.AddEquipmentViewModel
import co.kluvaka.cmp.equipment.ui.equipments.EquipmentsViewModel
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
    GetAllEquipmentsImpl(repository = get())
  }
  single<AddEquipment> {
    AddEquipmentImpl(repository = get())
  }

  viewModel {
    EquipmentsViewModel(getAllEquipments = get())
  }
  viewModel {
    AddEquipmentViewModel(addEquipment = get())
  }
}