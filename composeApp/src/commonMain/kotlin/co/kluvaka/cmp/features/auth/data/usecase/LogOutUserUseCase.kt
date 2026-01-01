package co.kluvaka.cmp.features.auth.data.usecase

import co.kluvaka.cmp.features.auth.domain.repository.AuthRepository
import co.kluvaka.cmp.features.auth.domain.usecase.LogOutUser

class LogOutUserUseCase(
    private val repository: AuthRepository,
) : LogOutUser {
    override suspend fun invoke() = repository.signOut()
}

