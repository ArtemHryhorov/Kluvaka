package co.kluvaka.cmp.features.auth.domain.usecase

fun interface LogOutUser {
    suspend operator fun invoke(): Result<Unit>
}

