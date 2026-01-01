package co.kluvaka.cmp.features.auth.data.usecase

import co.kluvaka.cmp.features.auth.domain.repository.AuthRepository
import co.kluvaka.cmp.features.auth.domain.usecase.SignUpUser

class SignUpUserUseCase(
    private val repository: AuthRepository,
) : SignUpUser {
    override suspend fun invoke(email: String, password: String) =
        repository.signUpWithEmailAndPassword(email, password)
}

