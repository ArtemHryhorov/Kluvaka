package co.kluvaka.cmp.di

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

val sessionModule = module {
  // Repository
  single<FishingSessionRepository> {
    FishingSessionRepositoryImpl(databaseDriverFactory = get())
  }

  // UseCase
  single<GetAllFishingSessions> {
    GetAllFishingSessionsUseCase(repository = get())
  }
  single<CreateFishingSession> {
    CreateFishingSessionUseCase(repository = get())
  }

  // ViewModel
  viewModel {
    StartSessionViewModel(createFishingSession = get())
  }
  viewModel {
    SessionsHistoryViewModel(getAllFishingSessions = get())
  }
}