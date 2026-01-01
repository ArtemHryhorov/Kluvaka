package co.kluvaka.cmp.features.auth.ui.signin

data class SignInState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val googleSignInError: String? = null,
)

