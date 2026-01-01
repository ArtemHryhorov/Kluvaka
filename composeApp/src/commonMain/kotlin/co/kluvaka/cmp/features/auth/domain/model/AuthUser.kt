package co.kluvaka.cmp.features.auth.domain.model

data class AuthUser(
    val uid: String,
    val email: String?,
    val displayName: String?,
)

