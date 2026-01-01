package co.kluvaka.cmp.features.auth.data.repository

import androidx.compose.runtime.Composable
import co.kluvaka.cmp.features.auth.domain.model.AuthUser

@Composable
actual fun rememberGoogleSignInLauncher(
    onResult: (Result<AuthUser>) -> Unit
): () -> Unit {
    return {
        onResult(Result.failure(UnsupportedOperationException("Google Sign In not implemented for iOS yet")))
    }
}

