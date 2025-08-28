package co.kluvaka.cmp

import co.kluvaka.cmp.equipment.data.EquipmentRepositoryImpl
import co.kluvaka.cmp.equipment.domain.EquipmentRepository
import co.kluvaka.cmp.equipment.domain.usecase.AddEquipment
import co.kluvaka.cmp.equipment.domain.usecase.AddEquipmentImpl
import co.kluvaka.cmp.equipment.domain.usecase.DeleteEquipment
import co.kluvaka.cmp.equipment.domain.usecase.DeleteEquipmentImpl
import co.kluvaka.cmp.equipment.domain.usecase.GetAllEquipments
import co.kluvaka.cmp.equipment.domain.usecase.GetAllEquipmentsImpl
import co.kluvaka.cmp.equipment.ui.add.equipment.AddEquipmentViewModel
import co.kluvaka.cmp.equipment.ui.equipments.EquipmentsViewModel
import co.kluvaka.cmp.sessions.data.FishingSessionRepositoryImpl
import co.kluvaka.cmp.sessions.domain.FishingSessionRepository
import co.kluvaka.cmp.sessions.domain.usecase.CreateFishingSession
import co.kluvaka.cmp.sessions.domain.usecase.CreateFishingSessionImpl
import co.kluvaka.cmp.sessions.domain.usecase.GetAllFishingSessions
import co.kluvaka.cmp.sessions.domain.usecase.GetAllFishingSessionsImpl
import co.kluvaka.cmp.sessions.ui.history.SessionsHistoryViewModel
import co.kluvaka.cmp.sessions.ui.start.session.StartSessionViewModel
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
    GetAllEquipmentsImpl(repository = get())
  }
  single<AddEquipment> {
    AddEquipmentImpl(repository = get())
  }
  single<DeleteEquipment> {
    DeleteEquipmentImpl(repository = get())
  }
  single<GetAllFishingSessions> {
    GetAllFishingSessionsImpl(repository = get())
  }
  single<CreateFishingSession> {
    CreateFishingSessionImpl(repository = get())
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