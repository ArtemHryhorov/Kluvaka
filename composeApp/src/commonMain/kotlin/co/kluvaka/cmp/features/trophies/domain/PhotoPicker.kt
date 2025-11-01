package co.kluvaka.cmp.features.trophies.domain

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

interface PhotoPicker {
    fun pickFromGallery(onResult: (String?) -> Unit)
    fun pickFromCamera(onResult: (String?) -> Unit)
}

@Composable
expect fun rememberPhotoPicker(): PhotoPicker
