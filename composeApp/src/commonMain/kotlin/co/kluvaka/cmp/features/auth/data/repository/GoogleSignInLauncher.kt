package co.kluvaka.cmp.features.auth.data.repository

import androidx.compose.runtime.Composable
import co.kluvaka.cmp.features.auth.domain.model.AuthUser

/**
 * Platform-specific Google Sign In launcher.
 * Returns a function that can be called to initiate Google Sign In.
 */
@Composable
expect fun rememberGoogleSignInLauncher(
    onResult: (Result<AuthUser>) -> Unit
): () -> Unit

