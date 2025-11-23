import UIKit
import PhotosUI
import ComposeApp

class ImagePickerImplementation: NSObject, NativeImagePicker, PHPickerViewControllerDelegate, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    private var singleImageCallback: ((String?) -> Void)?
    private var multipleImageCallback: (([String]) -> Void)?
    
    func pickImage(onResult: @escaping (String?) -> Void) {
        self.singleImageCallback = onResult
        self.multipleImageCallback = nil
        
        if #available(iOS 14, *) {
            var config = PHPickerConfiguration()
            config.selectionLimit = 1
            config.filter = .images
            
            let picker = PHPickerViewController(configuration: config)
            picker.delegate = self
            present(picker)
        } else {
            // Fallback for iOS 13
            let picker = UIImagePickerController()
            picker.sourceType = .photoLibrary
            picker.delegate = self
            present(picker)
        }
    }
    
    func pickImages(onResult: @escaping ([String]) -> Void) {
        self.multipleImageCallback = onResult
        self.singleImageCallback = nil
        
        if #available(iOS 14, *) {
            var config = PHPickerConfiguration()
            config.selectionLimit = 0 // 0 means no limit
            config.filter = .images
            
            let picker = PHPickerViewController(configuration: config)
            picker.delegate = self
            present(picker)
        } else {
            // iOS 13 doesn't support multi-select natively in this way easily
            // Just pick one for now or use 3rd party. Fallback to single
            let picker = UIImagePickerController()
            picker.sourceType = .photoLibrary
            picker.delegate = self
            present(picker)
        }
    }
    
    func pickFromCamera(onResult: @escaping (String?) -> Void) {
        self.singleImageCallback = onResult
        self.multipleImageCallback = nil
        
        let picker = UIImagePickerController()
        picker.sourceType = .camera
        picker.delegate = self
        present(picker)
    }
    
    private func present(_ viewController: UIViewController) {
        DispatchQueue.main.async {
            guard let rootVC = UIApplication.shared.keyWindow?.rootViewController else {
                return
            }
            rootVC.present(viewController, animated: true)
        }
    }
    
    // MARK: - PHPickerViewControllerDelegate
    
    @available(iOS 14, *)
    func picker(_ picker: PHPickerViewController, didFinishPicking results: [PHPickerResult]) {
        picker.dismiss(animated: true)
        
        if results.isEmpty {
            if let callback = singleImageCallback {
                callback(nil)
            } else if let callback = multipleImageCallback {
                callback([])
            }
            return
        }
        
        if let callback = singleImageCallback {
            // Handle single image
            if let result = results.first {
                loadItem(result.itemProvider) { path in
                    callback(path)
                }
            }
        } else if let callback = multipleImageCallback {
            // Handle multiple images
            let dispatchGroup = DispatchGroup()
            var paths: [String] = []
            
            for result in results {
                dispatchGroup.enter()
                loadItem(result.itemProvider) { path in
                    if let path = path {
                        paths.append(path)
                    }
                    dispatchGroup.leave()
                }
            }
            
            dispatchGroup.notify(queue: .main) {
                callback(paths)
            }
        }
    }
    
    private func loadItem(_ itemProvider: NSItemProvider, completion: @escaping (String?) -> Void) {
        if itemProvider.canLoadObject(ofClass: UIImage.self) {
            itemProvider.loadObject(ofClass: UIImage.self) { [weak self] image, error in
                guard let self = self, let image = image as? UIImage else {
                    DispatchQueue.main.async { completion(nil) }
                    return
                }
                let path = self.saveImageToDocuments(image: image)
                DispatchQueue.main.async { completion(path) }
            }
        } else {
            DispatchQueue.main.async { completion(nil) }
        }
    }
    
    // MARK: - UIImagePickerControllerDelegate
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        picker.dismiss(animated: true)
        
        if let image = info[.originalImage] as? UIImage {
            let path = saveImageToDocuments(image: image)
            
            if let callback = singleImageCallback {
                callback(path)
            } else if let callback = multipleImageCallback, let p = path {
                callback([p])
            }
        } else {
            if let callback = singleImageCallback {
                callback(nil)
            } else if let callback = multipleImageCallback {
                callback([])
            }
        }
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true)
        if let callback = singleImageCallback {
            callback(nil)
        } else if let callback = multipleImageCallback {
            callback([])
        }
    }
    
    private func saveImageToDocuments(image: UIImage) -> String? {
        guard let imageData = image.jpegData(compressionQuality: 0.8) else {
            return nil
        }
        
        let timestamp = Int64(Date().timeIntervalSince1970 * 1000)
        let uuid = UUID().uuidString
        let fileName = "trophy_\(timestamp)_\(uuid).jpg"
        
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

