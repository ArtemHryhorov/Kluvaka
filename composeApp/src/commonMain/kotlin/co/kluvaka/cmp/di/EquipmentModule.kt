package co.kluvaka.cmp.di

import co.kluvaka.cmp.database.EquipmentDatabase
import co.kluvaka.cmp.features.equipment.data.repository.EquipmentRepositoryImpl
import co.kluvaka.cmp.features.equipment.data.usecase.AddEquipmentUseCase
import co.kluvaka.cmp.features.equipment.data.usecase.DeleteEquipmentUseCase
import co.kluvaka.cmp.features.equipment.data.usecase.GetAllEquipmentsUseCase
import co.kluvaka.cmp.features.equipment.data.usecase.GetEquipmentByIdUseCase
import co.kluvaka.cmp.features.equipment.data.usecase.UpdateEquipmentUseCase
import co.kluvaka.cmp.features.equipment.domain.repository.EquipmentRepository
import co.kluvaka.cmp.features.equipment.domain.usecase.AddEquipment
import co.kluvaka.cmp.features.equipment.domain.usecase.DeleteEquipment
import co.kluvaka.cmp.features.equipment.domain.usecase.GetAllEquipments
import co.kluvaka.cmp.features.equipment.domain.usecase.GetEquipmentById
import co.kluvaka.cmp.features.equipment.domain.usecase.UpdateEquipment
import co.kluvaka.cmp.features.equipment.ui.add.equipment.AddEquipmentViewModel
import co.kluvaka.cmp.features.equipment.ui.details.EquipmentDetailsViewModel
import co.kluvaka.cmp.features.equipment.ui.equipments.EquipmentsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val equipmentModule = module {
  // Database
  single<EquipmentDatabase> {
    EquipmentDatabase(databaseDriverFactory = get())
  }

  // Repository
  single<EquipmentRepository> {
    EquipmentRepositoryImpl(database = get())
  }

  // UseCase
  single<GetAllEquipments> {
    GetAllEquipmentsUseCase(repository = get())
  }
  single<GetEquipmentById> {
    GetEquipmentByIdUseCase(repository = get())
  }
  single<AddEquipment> {
    AddEquipmentUseCase(repository = get())
  }
  single<DeleteEquipment> {
    DeleteEquipmentUseCase(repository = get())
  }
  single<UpdateEquipment> {
    UpdateEquipmentUseCase(repository = get())
  }

  // ViewModel
  viewModel {
    EquipmentsViewModel(
      getAllEquipments = get(),
      deleteEquipment = get(),
    )
  }
  viewModel {
    EquipmentDetailsViewModel(getEquipmentById = get())
  }
  viewModel {
    AddEquipmentViewModel(
      addEquipment = get(),
      updateEquipment = get(),
    )
  }
}