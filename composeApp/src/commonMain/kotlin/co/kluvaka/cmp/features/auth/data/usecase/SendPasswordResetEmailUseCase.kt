package co.kluvaka.cmp.features.auth.data.usecase

import co.kluvaka.cmp.features.auth.domain.repository.AuthRepository
import co.kluvaka.cmp.features.auth.domain.usecase.SendPasswordResetEmail

class SendPasswordResetEmailUseCase(
    private val repository: AuthRepository,
) : SendPasswordResetEmail {
    override suspend fun invoke(email: String) = repository.sendPasswordResetEmail(email)
}

