package co.kluvaka.cmp.di

import co.kluvaka.cmp.database.TrophyDatabase
import co.kluvaka.cmp.features.trophies.data.repository.TrophyRepositoryImpl
import co.kluvaka.cmp.features.trophies.data.usecase.AddTrophyUseCase
import co.kluvaka.cmp.features.trophies.data.usecase.DeleteTrophyUseCase
import co.kluvaka.cmp.features.trophies.data.usecase.GetAllTrophiesUseCase
import co.kluvaka.cmp.features.trophies.data.usecase.GetTrophyByIdUseCase
import co.kluvaka.cmp.features.trophies.data.usecase.UpdateTrophyUseCase
import co.kluvaka.cmp.features.trophies.domain.repository.TrophyRepository
import co.kluvaka.cmp.features.trophies.domain.usecase.AddTrophy
import co.kluvaka.cmp.features.trophies.domain.usecase.DeleteTrophy
import co.kluvaka.cmp.features.trophies.domain.usecase.GetAllTrophies
import co.kluvaka.cmp.features.trophies.domain.usecase.GetTrophyById
import co.kluvaka.cmp.features.trophies.domain.usecase.UpdateTrophy
import co.kluvaka.cmp.features.trophies.ui.add.trophy.AddTrophyViewModel
import co.kluvaka.cmp.features.trophies.ui.details.TrophyDetailsReducer
import co.kluvaka.cmp.features.trophies.ui.details.TrophyDetailsViewModel
import co.kluvaka.cmp.features.trophies.ui.trophies.TrophiesReducer
import co.kluvaka.cmp.features.trophies.ui.trophies.TrophiesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val trophyModule = module {
  // Database
  single<TrophyDatabase> {
    TrophyDatabase(databaseDriverFactory = get())
  }

  // Repository
  single<TrophyRepository> {
    TrophyRepositoryImpl(database = get())
  }

  // UseCase
  single<GetAllTrophies> {
    GetAllTrophiesUseCase(repository = get())
  }
  single<AddTrophy> {
    AddTrophyUseCase(repository = get())
  }
  single<DeleteTrophy> {
    DeleteTrophyUseCase(repository = get())
  }
  single<GetTrophyById> {
    GetTrophyByIdUseCase(repository = get())
  }
  single<UpdateTrophy> {
    UpdateTrophyUseCase(repository = get())
  }

  // Reducer
  single { TrophiesReducer() }
  single { TrophyDetailsReducer() }

  // ViewModel
  viewModel {
    TrophiesViewModel(
      getAllTrophies = get(),
      deleteTrophy = get(),
      reducer = get(),
    )
  }
  viewModel {
    AddTrophyViewModel(
      addTrophy = get(),
      updateTrophy = get(),
    )
  }
  viewModel {
    TrophyDetailsViewModel(
      getTrophyById = get(),
      reducer = get(),
    )
  }
}
