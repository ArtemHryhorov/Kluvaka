package co.kluvaka.cmp.di

import co.kluvaka.cmp.features.equipment.data.repository.EquipmentRepositoryImpl
import co.kluvaka.cmp.features.equipment.data.usecase.AddEquipmentUseCase
import co.kluvaka.cmp.features.equipment.data.usecase.DeleteEquipmentUseCase
import co.kluvaka.cmp.features.equipment.data.usecase.GetAllEquipmentsUseCase
import co.kluvaka.cmp.features.equipment.domain.repository.EquipmentRepository
import co.kluvaka.cmp.features.equipment.domain.usecase.AddEquipment
import co.kluvaka.cmp.features.equipment.domain.usecase.DeleteEquipment
import co.kluvaka.cmp.features.equipment.domain.usecase.GetAllEquipments
import co.kluvaka.cmp.features.equipment.ui.add.equipment.AddEquipmentViewModel
import co.kluvaka.cmp.features.equipment.ui.equipments.EquipmentsViewModel
import co.kluvaka.cmp.features.sessions.data.repository.FishingSessionRepositoryImpl
import co.kluvaka.cmp.features.sessions.data.usecase.CreateFishingSessionUseCase
import co.kluvaka.cmp.features.sessions.data.usecase.GetAllFishingSessionsUseCase
import co.kluvaka.cmp.features.sessions.domain.repository.FishingSessionRepository
import co.kluvaka.cmp.features.sessions.domain.usecase.CreateFishingSession
import co.kluvaka.cmp.features.sessions.domain.usecase.GetAllFishingSessions
import co.kluvaka.cmp.features.sessions.ui.history.SessionsHistoryViewModel
import co.kluvaka.cmp.features.sessions.ui.start.session.StartSessionViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val sharedModule = module {

  single<EquipmentRepository> {
    EquipmentRepositoryImpl(databaseDriverFactory = get())
  }
  single<FishingSessionRepository> {
    FishingSessionRepositoryImpl(databaseDriverFactory = get())
  }

  single<GetAllEquipments> {
    GetAllEquipmentsUseCase(repository = get())
  }
  single<AddEquipment> {
    AddEquipmentUseCase(repository = get())
  }
  single<DeleteEquipment> {
    DeleteEquipmentUseCase(repository = get())
  }
  single<GetAllFishingSessions> {
    GetAllFishingSessionsUseCase(repository = get())
  }
  single<CreateFishingSession> {
    CreateFishingSessionUseCase(repository = get())
  }

  viewModel {
    EquipmentsViewModel(
      getAllEquipments = get(),
      deleteEquipment = get(),
    )
  }
  viewModel {
    AddEquipmentViewModel(addEquipment = get())
  }
  viewModel {
    StartSessionViewModel(createFishingSession = get())
  }
  viewModel {
    SessionsHistoryViewModel(getAllFishingSessions = get())
  }
}