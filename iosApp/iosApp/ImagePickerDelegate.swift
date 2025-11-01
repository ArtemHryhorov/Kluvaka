import UIKit
import Foundation

@objc class ImagePickerDelegate: NSObject, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    private var onImageSelected: ((String?) -> Void)?
    private var onCancelled: (() -> Void)?
    
    @objc func setCallbacks(onImageSelected: @escaping (String?) -> Void, onCancelled: @escaping () -> Void) {
        self.onImageSelected = onImageSelected
        self.onCancelled = onCancelled
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        
        picker.dismiss(animated: true) {
            if let image = info[.originalImage] as? UIImage {
                let imagePath = self.saveImageToDocuments(image: image)
                self.onImageSelected?(imagePath)
            } else {
                self.onImageSelected?(nil)
            }
        }
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true) {
            self.onCancelled?()
        }
    }
    
    private func saveImageToDocuments(image: UIImage) -> String? {
        guard let imageData = image.jpegData(compressionQuality: 0.8) else {
            return nil
        }
        
        let timestamp = Int64(Date().timeIntervalSince1970 * 1000)
        let fileName = "trophy_\(timestamp).jpg"
        
        guard let documentsURL = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first else {
            return nil
        }
        
        let imageURL = documentsURL.appendingPathComponent(fileName)
        
        do {
            try imageData.write(to: imageURL)
            return "file://\(imageURL.path)"
        } catch {
            print("Error saving image: \(error)")
            return nil
        }
    }
}
