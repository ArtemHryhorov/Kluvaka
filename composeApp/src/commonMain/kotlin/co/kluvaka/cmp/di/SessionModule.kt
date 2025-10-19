package co.kluvaka.cmp.di

import co.kluvaka.cmp.database.SessionDatabase
import co.kluvaka.cmp.features.sessions.data.repository.FishingSessionRepositoryImpl
import co.kluvaka.cmp.features.sessions.data.usecase.CreateFishingSessionUseCase
import co.kluvaka.cmp.features.sessions.data.usecase.FinishActiveSessionUseCase
import co.kluvaka.cmp.features.sessions.data.usecase.GetActiveFishingSessionUseCase
import co.kluvaka.cmp.features.sessions.data.usecase.GetAllFishingSessionsUseCase
import co.kluvaka.cmp.features.sessions.data.usecase.GetSessionByIdUseCase
import co.kluvaka.cmp.features.sessions.domain.repository.FishingSessionRepository
import co.kluvaka.cmp.features.sessions.domain.usecase.CreateFishingSession
import co.kluvaka.cmp.features.sessions.domain.usecase.FinishActiveSession
import co.kluvaka.cmp.features.sessions.domain.usecase.GetActiveFishingSession
import co.kluvaka.cmp.features.sessions.domain.usecase.GetAllFishingSessions
import co.kluvaka.cmp.features.sessions.domain.usecase.GetSessionById
import co.kluvaka.cmp.features.sessions.ui.active.ActiveSessionViewModel
import co.kluvaka.cmp.features.sessions.ui.completed.CompletedSessionViewModel
import co.kluvaka.cmp.features.sessions.ui.history.SessionsHistoryViewModel
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

  // ViewModel
  viewModel {
    ActiveSessionViewModel(
      getActiveFishingSession = get(),
      getSessionById = get(),
      finishActiveSession = get(),
    )
  }
  viewModel {
    SessionsHistoryViewModel(getAllFishingSessions = get())
  }
  viewModel {
    StartSessionViewModel(createFishingSession = get())
  }
  viewModel {
    CompletedSessionViewModel(getSessionById = get())
  }
}