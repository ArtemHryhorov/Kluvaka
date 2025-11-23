import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    
    init() {
        ImagePickerProvider.shared.register(picker: ImagePickerImplementation())
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}