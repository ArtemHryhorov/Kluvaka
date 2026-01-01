package co.kluvaka.cmp.features.auth.domain.usecase

import co.kluvaka.cmp.features.auth.domain.model.AuthUser

fun interface LoginUser {
    suspend operator fun invoke(email: String, password: String): Result<AuthUser>
}

