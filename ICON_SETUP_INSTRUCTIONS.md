# App Icon Setup Instructions

## Android Icons ✅
The Android adaptive icons have been created and are ready to use:
- **Foreground**: `composeApp/src/androidMain/res/drawable-v24/ic_launcher_foreground.xml`
- **Background**: `composeApp/src/androidMain/res/drawable/ic_launcher_background.xml`

The icons feature a fishing rod with a golden fish on a blue water-themed background, perfect for your fishing app "Kluvaka".

## iOS Icon Setup

To set up the iOS icon, you need to convert the SVG to PNG format:

### Option 1: Using Online Converter (Easiest)
1. Open the SVG file: `iosApp/iosApp/Assets.xcassets/AppIcon.appiconset/app-icon.svg`
2. Use an online SVG to PNG converter (e.g., https://cloudconvert.com/svg-to-png)
3. Set the output size to **1024x1024 pixels**
4. Download the PNG file
5. Rename it to `app-icon-1024.png`
6. Replace the existing file in `iosApp/iosApp/Assets.xcassets/AppIcon.appiconset/`

### Option 2: Using ImageMagick (Command Line)
```bash
cd iosApp/iosApp/Assets.xcassets/AppIcon.appiconset/
convert -background none -resize 1024x1024 app-icon.svg app-icon-1024.png
```

### Option 3: Using Xcode
1. Open Xcode
2. Navigate to `iosApp/iosApp/Assets.xcassets/AppIcon.appiconset/`
3. Drag and drop the SVG file into the 1024x1024 slot
4. Xcode will automatically convert it

### Option 4: Using Inkscape (Free Desktop App)
```bash
inkscape --export-type=png --export-width=1024 --export-height=1024 app-icon.svg
```

## Icon Design
The icon features:
- **Fishing rod** with a golden fish on the hook
- **Blue water gradient background** with wave patterns
- **Clean, modern design** that represents fishing activities
- **High contrast** for visibility on various backgrounds

## Testing
After setting up the icons:
1. **Android**: Rebuild the app and check the launcher icon
2. **iOS**: Build in Xcode and verify the icon appears correctly on the home screen

## Notes
- The Android adaptive icon will automatically adapt to different device shapes
- The iOS icon should be exactly 1024x1024 pixels without any transparency
- Both icons use the same design theme for consistency across platforms

