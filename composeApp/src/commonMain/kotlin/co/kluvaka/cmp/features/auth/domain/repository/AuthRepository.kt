package co.kluvaka.cmp.features.auth.domain.repository

import co.kluvaka.cmp.features.auth.domain.model.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: AuthUser?
    
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<AuthUser>
    suspend fun signUpWithEmailAndPassword(email: String, password: String): Result<AuthUser>
    suspend fun signInWithGoogle(): Result<AuthUser>
    suspend fun signOut(): Result<Unit>
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
    suspend fun getIdToken(): Result<String>
    fun observeAuthState(): Flow<AuthUser?>
}

