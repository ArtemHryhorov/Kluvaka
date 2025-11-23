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

class IOSPhotoPicker : PhotoPicker {
    
    override fun pickFromGallery(onResult: (String?) -> Unit) {
        ImagePickerProvider.get()?.pickImage(onResult)
    }

    override fun pickMultipleFromGallery(onResult: (List<String>) -> Unit) {
        ImagePickerProvider.get()?.pickImages(onResult)
    }

    override fun pickFromCamera(onResult: (String?) -> Unit) {
        ImagePickerProvider.get()?.pickFromCamera(onResult)
    }
}

@Composable
actual fun rememberPhotoPicker(): PhotoPicker {
    return remember { IOSPhotoPicker() }
}
