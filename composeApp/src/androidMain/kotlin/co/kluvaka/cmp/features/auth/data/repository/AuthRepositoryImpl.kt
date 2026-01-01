package co.kluvaka.cmp.features.auth.data.repository

import co.kluvaka.cmp.features.auth.domain.model.AuthUser
import co.kluvaka.cmp.features.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : AuthRepository {
    
    override val currentUser: AuthUser?
        get() = firebaseAuth.currentUser?.toAuthUser()
    
    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<AuthUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user?.toAuthUser() ?: throw IllegalStateException("User is null"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): Result<AuthUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user?.toAuthUser() ?: throw IllegalStateException("User is null"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun signInWithGoogle(): Result<AuthUser> {
        // Google Sign In requires Activity result handling, so it's handled via GoogleSignInLauncher
        // This method is kept for interface compliance but should not be called directly
        return Result.failure(UnsupportedOperationException("Use GoogleSignInLauncher for Google Sign In"))
    }
    
    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getIdToken(): Result<String> {
        return try {
            val user = firebaseAuth.currentUser
            if (user == null) {
                Result.failure(IllegalStateException("No user signed in"))
            } else {
                val token = user.getIdToken(false).await()
                Result.success(token.token ?: "")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun observeAuthState(): Flow<AuthUser?> {
        return callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                trySend(auth.currentUser?.toAuthUser())
            }
            firebaseAuth.addAuthStateListener(listener)
            
            awaitClose {
                firebaseAuth.removeAuthStateListener(listener)
            }
        }
    }
    
    private fun FirebaseUser.toAuthUser(): AuthUser {
        return AuthUser(
            uid = uid,
            email = email,
            displayName = displayName
        )
    }
}

