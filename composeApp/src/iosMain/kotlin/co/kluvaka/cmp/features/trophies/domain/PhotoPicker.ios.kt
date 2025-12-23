package co.kluvaka.cmp.features.trophies.domain

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

interface NativeImagePicker {
    fun pickImage(onResult: (String?) -> Unit)
    fun pickImages(onResult: (List<String>) -> Unit)
    fun pickFromCamera(onResult: (String?) -> Unit)
}

object ImagePickerProvider {
    private var picker: NativeImagePicker? = null

    fun register(picker: NativeImagePicker) {
        this.picker = picker
    }

    fun get(): NativeImagePicker? = picker
}

class IOSPhotoPicker(
    private val permissionManager: PermissionManager
) : PhotoPicker {
    
    override fun pickFromGallery(onResult: (String?) -> Unit) {
        // iOS handles permissions automatically via Info.plist
        // The system will prompt when accessing photo library
        permissionManager.requestGalleryPermission { granted ->
            if (granted) {
                ImagePickerProvider.get()?.pickImage(onResult)
            } else {
                onResult(null)
            }
        }
    }

    override fun pickMultipleFromGallery(onResult: (List<String>) -> Unit) {
        // iOS handles permissions automatically via Info.plist
        // The system will prompt when accessing photo library
        permissionManager.requestGalleryPermission { granted ->
            if (granted) {
                ImagePickerProvider.get()?.pickImages(onResult)
            } else {
                onResult(emptyList())
            }
        }
    }

    override fun pickFromCamera(onResult: (String?) -> Unit) {
        // iOS handles permissions automatically via Info.plist
        // The system will prompt when accessing camera
        permissionManager.requestCameraPermission { granted ->
            if (granted) {
                ImagePickerProvider.get()?.pickFromCamera(onResult)
            } else {
                onResult(null)
            }
        }
    }
}

@Composable
actual fun rememberPhotoPicker(): PhotoPicker {
    val permissionManager = rememberPermissionManager()
    return remember { IOSPhotoPicker(permissionManager) }
}
