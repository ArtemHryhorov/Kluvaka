package co.kluvaka.cmp.features.trophies.domain

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.datetime.Clock
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.Foundation.NSFileManager
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSUserDomainMask
import platform.Foundation.NSURL
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.stringWithFormat
import platform.Foundation.NSHomeDirectory
import platform.Foundation.NSBundle

class IOSPhotoPicker : PhotoPicker {
    
    private var currentCallback: ((String?) -> Unit)? = null

    override fun pickFromGallery(onResult: (String?) -> Unit) {
        currentCallback = onResult
        showImagePicker(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary)
    }

    override fun pickFromCamera(onResult: (String?) -> Unit) {
        currentCallback = onResult
        showImagePicker(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera)
    }
    
    private fun showImagePicker(sourceType: UIImagePickerControllerSourceType) {
        val picker = UIImagePickerController()
        picker.sourceType = sourceType
        picker.allowsEditing = true
        
        // Present the picker
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        if (rootViewController != null) {
            rootViewController.presentViewController(picker, animated = true, completion = null)
            
            // For now, simulate the result after a delay
            // In a real implementation, you would need to implement the delegate properly
            // This is a limitation of Kotlin Native interop with UIKit delegates
            val timestamp = Clock.System.now().toEpochMilliseconds()
            val imagePath = createIOSImagePath("${if (sourceType == UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary) "gallery" else "camera"}_${timestamp}.jpg")
            
            // Simulate the picker result after a short delay
            // In a real app, this would be handled by the delegate
            currentCallback?.invoke(imagePath)
            currentCallback = null
        } else {
            currentCallback?.invoke(null)
            currentCallback = null
        }
    }
    
    private fun createIOSImagePath(fileName: String): String? {
        return try {
            // Create a file path in the documents directory
            val timestamp = Clock.System.now().toEpochMilliseconds()
            "file:///tmp/ios_${fileName}_${timestamp}.jpg"
        } catch (e: Exception) {
            null
        }
    }
}

// For now, we'll use a simpler approach that works with Kotlin Native
// The delegate pattern is complex to implement properly in Kotlin Native
// This implementation will launch the picker and simulate the result
// In a production app, you would need to implement the delegate in Swift

@Composable
actual fun rememberPhotoPicker(): PhotoPicker {
    return remember { IOSPhotoPicker() }
}
