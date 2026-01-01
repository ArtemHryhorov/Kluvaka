package co.kluvaka.cmp.features.auth.domain.usecase

fun interface SendPasswordResetEmail {
    suspend operator fun invoke(email: String): Result<Unit>
}

