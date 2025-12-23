package co.kluvaka.cmp.features.trophies.domain

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
actual fun rememberPermissionManager(): PermissionManager {
    val context = LocalContext.current
    
    val cameraCallbackRef = remember { mutableStateOf<((Boolean) -> Unit)?>(null) }
    val galleryCallbackRef = remember { mutableStateOf<((Boolean) -> Unit)?>(null) }
    
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        cameraCallbackRef.value?.invoke(granted)
        cameraCallbackRef.value = null
    }
    
    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        galleryCallbackRef.value?.invoke(granted)
        galleryCallbackRef.value = null
    }
    
    return remember {
        object : PermissionManager {
            override fun hasCameraPermission(): Boolean {
                return ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            }
            
            override fun hasGalleryPermission(): Boolean {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) == PackageManager.PERMISSION_GRANTED
                } else {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                }
            }
            
            override fun requestCameraPermission(onResult: (Boolean) -> Unit) {
                if (hasCameraPermission()) {
                    onResult(true)
                } else {
                    cameraCallbackRef.value = onResult
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
            
            override fun requestGalleryPermission(onResult: (Boolean) -> Unit) {
                if (hasGalleryPermission()) {
                    onResult(true)
                } else {
                    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        Manifest.permission.READ_MEDIA_IMAGES
                    } else {
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    }
                    galleryCallbackRef.value = onResult
                    galleryPermissionLauncher.launch(permission)
                }
            }
        }
    }
}

