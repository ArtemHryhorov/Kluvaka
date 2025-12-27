package co.kluvaka.cmp.di

import co.kluvaka.cmp.database.SessionDatabase
import co.kluvaka.cmp.features.sessions.data.repository.FishingSessionRepositoryImpl
import co.kluvaka.cmp.features.sessions.data.usecase.AddSessionEventUseCase
import co.kluvaka.cmp.features.sessions.data.usecase.CreateFishingSessionUseCase
import co.kluvaka.cmp.features.sessions.data.usecase.DeleteFishingSessionUseCase
import co.kluvaka.cmp.features.sessions.data.usecase.FinishActiveSessionUseCase
import co.kluvaka.cmp.features.sessions.data.usecase.GetActiveFishingSessionUseCase
import co.kluvaka.cmp.features.sessions.data.usecase.GetAllFishingSessionsUseCase
import co.kluvaka.cmp.features.sessions.data.usecase.GetSessionByIdUseCase
import co.kluvaka.cmp.features.sessions.data.usecase.GetSessionEventByIdUseCase
import co.kluvaka.cmp.features.sessions.domain.repository.FishingSessionRepository
import co.kluvaka.cmp.features.sessions.domain.usecase.AddSessionEvent
import co.kluvaka.cmp.features.sessions.domain.usecase.CreateFishingSession
import co.kluvaka.cmp.features.sessions.domain.usecase.DeleteFishingSession
import co.kluvaka.cmp.features.sessions.domain.usecase.FinishActiveSession
import co.kluvaka.cmp.features.sessions.domain.usecase.GetActiveFishingSession
import co.kluvaka.cmp.features.sessions.domain.usecase.GetAllFishingSessions
import co.kluvaka.cmp.features.sessions.domain.usecase.GetSessionById
import co.kluvaka.cmp.features.sessions.domain.usecase.GetSessionEventById
import co.kluvaka.cmp.features.sessions.ui.active.SessionViewModel
import co.kluvaka.cmp.features.sessions.ui.detail.DetailedSessionEventViewModel
import co.kluvaka.cmp.features.sessions.ui.sessions.SessionsReducer
import co.kluvaka.cmp.features.sessions.ui.sessions.SessionsViewModel
import co.kluvaka.cmp.features.sessions.ui.start.session.StartSessionViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val sessionModule = module {
  // Database
  single<SessionDatabase> {
    SessionDatabase(databaseDriverFactory = get())
  }

  // Repository
  single<FishingSessionRepository> {
    FishingSessionRepositoryImpl(database = get())
  }

  // UseCase
  single<CreateFishingSession> {
    CreateFishingSessionUseCase(repository = get())
  }
  single<FinishActiveSession> {
    FinishActiveSessionUseCase(repository = get())
  }
  single<GetActiveFishingSession> {
    GetActiveFishingSessionUseCase(repository = get())
  }
  single<GetAllFishingSessions> {
    GetAllFishingSessionsUseCase(repository = get())
  }
  single<GetSessionById> {
    GetSessionByIdUseCase(repository = get())
  }
  single<AddSessionEvent> {
    AddSessionEventUseCase(repository = get())
  }
  single<DeleteFishingSession> {
    DeleteFishingSessionUseCase(repository = get())
  }
  single<GetSessionEventById> {
    GetSessionEventByIdUseCase(repository = get())
  }

  // Reducer
  single<SessionsReducer> { SessionsReducer() }

  // ViewModel
  viewModel {
    SessionViewModel(
      getActiveFishingSession = get(),
      getSessionById = get(),
      finishActiveSession = get(),
      addSessionEvent = get(),
    )
  }
  viewModel {
    SessionsViewModel(
      getAllFishingSessions = get(),
      deleteFishingSession = get(),
      reducer = get(),
    )
  }
  viewModel {
    StartSessionViewModel(createFishingSession = get())
  }
  viewModel { (sessionId: Int, eventId: Int) ->
    DetailedSessionEventViewModel(
      getSessionById = get(),
      getSessionEventById = get(),
      sessionId = sessionId,
      eventId = eventId,
    )
  }
}