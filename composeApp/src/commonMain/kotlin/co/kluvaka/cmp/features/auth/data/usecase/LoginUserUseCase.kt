package co.kluvaka.cmp.features.auth.data.usecase

import co.kluvaka.cmp.features.auth.domain.repository.AuthRepository
import co.kluvaka.cmp.features.auth.domain.usecase.LoginUser

class LoginUserUseCase(
    private val repository: AuthRepository,
) : LoginUser {
    override suspend fun invoke(email: String, password: String) =
        repository.signInWithEmailAndPassword(email, password)
}

