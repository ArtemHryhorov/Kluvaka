package co.kluvaka.cmp.features.trophies.domain

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberPermissionManager(): PermissionManager {
    return remember {
        object : PermissionManager {
            override fun hasCameraPermission(): Boolean {
                // iOS handles permissions automatically via Info.plist
                // The system will prompt when accessing camera/photo library
                return true
            }
            
            override fun hasGalleryPermission(): Boolean {
                // iOS handles permissions automatically via Info.plist
                // The system will prompt when accessing camera/photo library
                return true
            }
            
            override fun requestCameraPermission(onResult: (Boolean) -> Unit) {
                // iOS handles permissions automatically when accessing camera
                // The system will prompt if needed
                onResult(true)
            }
            
            override fun requestGalleryPermission(onResult: (Boolean) -> Unit) {
                // iOS handles permissions automatically when accessing photo library
                // The system will prompt if needed
                onResult(true)
            }
        }
    }
}

