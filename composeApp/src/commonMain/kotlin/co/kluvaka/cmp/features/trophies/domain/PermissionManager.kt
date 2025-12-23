package co.kluvaka.cmp.features.trophies.domain

import androidx.compose.runtime.Composable

interface PermissionManager {
    fun hasCameraPermission(): Boolean
    fun hasGalleryPermission(): Boolean
    fun requestCameraPermission(onResult: (Boolean) -> Unit)
    fun requestGalleryPermission(onResult: (Boolean) -> Unit)
}

@Composable
expect fun rememberPermissionManager(): PermissionManager

