package co.kluvaka.cmp.features.auth.data.usecase

import co.kluvaka.cmp.features.auth.domain.repository.AuthRepository
import co.kluvaka.cmp.features.auth.domain.usecase.GetCurrentUser

class GetCurrentUserUseCase(
    private val repository: AuthRepository,
) : GetCurrentUser {
    override fun invoke() = repository.currentUser
}

