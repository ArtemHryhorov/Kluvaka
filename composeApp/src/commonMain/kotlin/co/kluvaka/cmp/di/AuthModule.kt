package co.kluvaka.cmp.di

import co.kluvaka.cmp.features.auth.data.usecase.GetCurrentUserUseCase
import co.kluvaka.cmp.features.auth.data.usecase.LoginUserUseCase
import co.kluvaka.cmp.features.auth.data.usecase.LogOutUserUseCase
import co.kluvaka.cmp.features.auth.data.usecase.SendPasswordResetEmailUseCase
import co.kluvaka.cmp.features.auth.data.usecase.SignInWithGoogleUseCase
import co.kluvaka.cmp.features.auth.data.usecase.SignUpUserUseCase
import co.kluvaka.cmp.features.auth.domain.repository.AuthRepository
import co.kluvaka.cmp.features.auth.domain.usecase.GetCurrentUser
import co.kluvaka.cmp.features.auth.domain.usecase.LoginUser
import co.kluvaka.cmp.features.auth.domain.usecase.LogOutUser
import co.kluvaka.cmp.features.auth.domain.usecase.SendPasswordResetEmail
import co.kluvaka.cmp.features.auth.domain.usecase.SignInWithGoogle
import co.kluvaka.cmp.features.auth.domain.usecase.SignUpUser
import co.kluvaka.cmp.features.auth.ui.profile.ProfileViewModel
import co.kluvaka.cmp.features.auth.ui.signin.SignInViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    // Use Cases - depend on AuthRepository which is provided by platform-specific modules
    single<GetCurrentUser> {
        GetCurrentUserUseCase(repository = get())
    }
    
    single<LoginUser> {
        LoginUserUseCase(repository = get())
    }
    
    single<SignUpUser> {
        SignUpUserUseCase(repository = get())
    }
    
    single<SignInWithGoogle> {
        SignInWithGoogleUseCase(repository = get())
    }
    
    single<LogOutUser> {
        LogOutUserUseCase(repository = get())
    }
    
    single<SendPasswordResetEmail> {
        SendPasswordResetEmailUseCase(repository = get())
    }
    
    // ViewModels
    viewModel {
        SignInViewModel(
            loginUser = get(),
            signUpUser = get(),
            signInWithGoogle = get(),
            logOutUser = get(),
            authRepository = get(),
        )
    }
    
    viewModel {
        ProfileViewModel(
            logOutUser = get(),
            authRepository = get(),
        )
    }
}

