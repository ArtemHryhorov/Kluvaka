package co.kluvaka.cmp.features.auth.domain.usecase

import co.kluvaka.cmp.features.auth.domain.model.AuthUser

fun interface SignInWithGoogle {
    suspend operator fun invoke(): Result<AuthUser>
}

