package co.kluvaka.cmp.features.trophies.domain

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

class AndroidPhotoPicker(
    private val context: Context,
    private val permissionManager: PermissionManager
) : PhotoPicker {
    
    private var galleryCallback: ((String?) -> Unit)? = null
    private var galleryMultipleCallback: ((List<String>) -> Unit)? = null
    private var cameraCallback: ((String?) -> Unit)? = null
    private var galleryLauncher: androidx.activity.result.ActivityResultLauncher<String>? = null
    private var galleryMultipleLauncher: androidx.activity.result.ActivityResultLauncher<String>? = null
    private var cameraLauncher: androidx.activity.result.ActivityResultLauncher<Uri>? = null
    private var currentPhotoUri: Uri? = null
    
    fun setLaunchers(
        galleryLauncher: androidx.activity.result.ActivityResultLauncher<String>,
        galleryMultipleLauncher: androidx.activity.result.ActivityResultLauncher<String>,
        cameraLauncher: androidx.activity.result.ActivityResultLauncher<Uri>
    ) {
        this.galleryLauncher = galleryLauncher
        this.galleryMultipleLauncher = galleryMultipleLauncher
        this.cameraLauncher = cameraLauncher
    }
    
    override fun pickFromGallery(onResult: (String?) -> Unit) {
        permissionManager.requestGalleryPermission { granted ->
            if (granted) {
                galleryCallback = onResult
                galleryLauncher?.launch("image/*")
            } else {
                onResult(null)
            }
        }
    }

    override fun pickMultipleFromGallery(onResult: (List<String>) -> Unit) {
        permissionManager.requestGalleryPermission { granted ->
            if (granted) {
                galleryMultipleCallback = onResult
                galleryMultipleLauncher?.launch("image/*")
            } else {
                onResult(emptyList())
            }
        }
    }
    
    override fun pickFromCamera(onResult: (String?) -> Unit) {
        permissionManager.requestCameraPermission { granted ->
            if (granted) {
                cameraCallback = onResult
                // Create a temporary file for the camera result
                val tempFile = File(context.cacheDir, "camera_${System.currentTimeMillis()}.jpg")
                // Use FileProvider to create a content URI
                currentPhotoUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    tempFile
                )
                cameraLauncher?.launch(currentPhotoUri!!)
            } else {
                onResult(null)
            }
        }
    }
    
    fun handleGalleryResult(uri: Uri?) {
        println("DEBUG: Gallery result URI: $uri")
        if (uri != null) {
            // Copy the image to permanent storage
            val permanentFile = copyImageToPermanentStorage(uri)
            val permanentUri = permanentFile?.let { FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", it) }
            val permanentUriString = permanentUri?.toString()
            println("DEBUG: Permanent URI: $permanentUriString")
            galleryCallback?.invoke(permanentUriString)
        } else {
            galleryCallback?.invoke(null)
        }
        galleryCallback = null
    }

    fun handleGalleryMultipleResult(uris: List<Uri>) {
        val resultPaths = mutableListOf<String>()
        for (uri in uris) {
            val permanentFile = copyImageToPermanentStorage(uri)
            val permanentUri = permanentFile?.let { FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", it) }
            permanentUri?.toString()?.let { resultPaths.add(it) }
        }
        galleryMultipleCallback?.invoke(resultPaths)
        galleryMultipleCallback = null
    }
    
    private fun copyImageToPermanentStorage(uri: Uri): File? {
        return try {
            // Create a permanent file in the app's files directory
            val permanentFile = File(context.filesDir, "trophy_${System.currentTimeMillis()}.jpg")
            
            // Copy the content from the temporary URI to the permanent file
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                permanentFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            
            println("DEBUG: Image copied to: ${permanentFile.absolutePath}")
            permanentFile
        } catch (e: Exception) {
            println("DEBUG: Error copying image: ${e.message}")
            null
        }
    }
    
    fun handleCameraResult(success: Boolean) {
        cameraCallback?.invoke(if (success) currentPhotoUri?.toString() else null)
        cameraCallback = null
    }
}

@Composable
actual fun rememberPhotoPicker(): PhotoPicker {
    val context = LocalContext.current
    val permissionManager = rememberPermissionManager()
    val photoPicker = remember { AndroidPhotoPicker(context, permissionManager) }
    
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        photoPicker.handleGalleryResult(uri)
    }

    val galleryMultipleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        photoPicker.handleGalleryMultipleResult(uris)
    }
    
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        photoPicker.handleCameraResult(success)
    }
    
    photoPicker.setLaunchers(galleryLauncher, galleryMultipleLauncher, cameraLauncher)
    
    return photoPicker
}
