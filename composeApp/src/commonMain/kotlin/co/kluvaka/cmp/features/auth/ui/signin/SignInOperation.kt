package co.kluvaka.cmp.features.auth.ui.signin

sealed interface SignInOperation {
    sealed interface Actions : SignInOperation {
        data class EmailUpdate(val email: String) : Actions
        data class PasswordUpdate(val password: String) : Actions
        data object Login : Actions
        data object SignUp : Actions
        data object SignInWithGoogle : Actions
        data class SetGoogleSignInError(val error: String?) : Actions
        data object LogOut : Actions
        data object ClearError : Actions
    }
    
    sealed interface Events : SignInOperation {
        data class LoginSuccess(val user: co.kluvaka.cmp.features.auth.domain.model.AuthUser) : Events
        data class LoginFailure(val error: Throwable) : Events
        data class SignUpSuccess(val user: co.kluvaka.cmp.features.auth.domain.model.AuthUser) : Events
        data class SignUpFailure(val error: Throwable) : Events
        data class GoogleSignInSuccess(val user: co.kluvaka.cmp.features.auth.domain.model.AuthUser) : Events
        data class GoogleSignInFailure(val error: Throwable) : Events
        data object LogOutSuccess : Events
        data class LogOutFailure(val error: Throwable) : Events
    }
}

