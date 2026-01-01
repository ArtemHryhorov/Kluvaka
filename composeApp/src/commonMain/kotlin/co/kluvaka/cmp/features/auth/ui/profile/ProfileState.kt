package co.kluvaka.cmp.features.auth.ui.profile

data class ProfileState(
    val userId: String? = null,
    val email: String? = null,
    val authToken: String? = null,
    val isLoading: Boolean = false,
)

