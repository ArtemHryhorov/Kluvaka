package co.kluvaka.cmp.features.auth.data.repository

import co.kluvaka.cmp.features.auth.domain.model.AuthUser
import co.kluvaka.cmp.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * iOS implementation of AuthRepository.
 * Currently a no-op implementation. Replace with Firebase iOS SDK integration when ready.
 */
class AuthRepositoryImpl : AuthRepository {
    override val currentUser: AuthUser? = null
    
    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<AuthUser> {
        return Result.failure(UnsupportedOperationException("Firebase Auth not implemented for iOS yet"))
    }
    
    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): Result<AuthUser> {
        return Result.failure(UnsupportedOperationException("Firebase Auth not implemented for iOS yet"))
    }
    
    override suspend fun signInWithGoogle(): Result<AuthUser> {
        return Result.failure(UnsupportedOperationException("Google Sign In not implemented for iOS yet"))
    }
    
    override suspend fun signOut(): Result<Unit> {
        return Result.success(Unit)
    }
    
    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return Result.failure(UnsupportedOperationException("Firebase Auth not implemented for iOS yet"))
    }
    
    override suspend fun getIdToken(): Result<String> {
        return Result.failure(UnsupportedOperationException("Firebase Auth not implemented for iOS yet"))
    }
    
    override fun observeAuthState(): Flow<AuthUser?> {
        return flowOf(null)
    }
}

