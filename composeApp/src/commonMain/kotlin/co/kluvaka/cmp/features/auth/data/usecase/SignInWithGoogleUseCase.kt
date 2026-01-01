package co.kluvaka.cmp.features.auth.data.usecase

import co.kluvaka.cmp.features.auth.domain.repository.AuthRepository
import co.kluvaka.cmp.features.auth.domain.usecase.SignInWithGoogle

class SignInWithGoogleUseCase(
    private val repository: AuthRepository,
) : SignInWithGoogle {
    override suspend fun invoke() = repository.signInWithGoogle()
}

