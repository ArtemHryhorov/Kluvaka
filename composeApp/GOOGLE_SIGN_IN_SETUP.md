# Google Sign In Setup Guide

## Step 1: Get SHA-1 Fingerprint

You need to add your app's SHA-1 fingerprint to Firebase Console.

### For Debug Build (Development):

**Option A: Using keytool command**
```bash
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

**Option B: Using Gradle task**
```bash
./gradlew signingReport
```

Look for the SHA-1 fingerprint in the output (it will look like: `AA:BB:CC:DD:EE:FF:...`)

### For Release Build (Production):

If you have a release keystore:
```bash
keytool -list -v -keystore /path/to/your/release.keystore -alias your-key-alias
```

## Step 2: Add SHA-1 to Firebase Console

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project: **kluvaka-app**
3. Click the gear icon ⚙️ → **Project Settings**
4. Scroll down to **Your apps** section
5. Find your Android app (package: `co.kluvaka.cmp`)
6. Click **Add fingerprint**
7. Paste your SHA-1 fingerprint
8. Click **Save**

## Step 3: Enable Google Sign In

1. In Firebase Console, go to **Authentication** → **Sign-in method**
2. Click on **Google** provider
3. Toggle **Enable** to ON
4. Enter a **Project support email** (your email)
5. Click **Save**

## Step 4: Get Web Client ID

After enabling Google Sign In:

1. Stay in the **Google** provider configuration page
2. Scroll down to **Web SDK configuration** section
3. Copy the **Web client ID** (it will look like: `123456789-abcdefghijklmnop.apps.googleusercontent.com`)

## Step 5: Configure in Code

1. Open `composeApp/src/androidMain/kotlin/co/kluvaka/cmp/features/auth/data/repository/GoogleSignInLauncher.android.kt`
2. Find the `getGoogleClientId` function (around line 92)
3. Replace `"YOUR_WEB_CLIENT_ID"` with your actual Web Client ID:

```kotlin
private fun getGoogleClientId(context: Context): String {
    // Replace with your Web Client ID from Firebase Console
    return "123456789-abcdefghijklmnop.apps.googleusercontent.com"
}
```

## Step 6: Download Updated google-services.json (Optional)

After configuring Google Sign In, Firebase may update your `google-services.json`:

1. Go to **Project Settings** → **Your apps**
2. Click on your Android app
3. Download the updated `google-services.json`
4. Replace `composeApp/google-services.json` with the new file

The `oauth_client` array should now be populated with your Web Client ID.

## Troubleshooting

- **Button does nothing**: Check Logcat for errors. Make sure SHA-1 is added and Google Sign In is enabled.
- **"Google Client ID not configured" error**: Make sure you've replaced `YOUR_WEB_CLIENT_ID` in the code.
- **"10: " error**: This usually means SHA-1 fingerprint is missing or incorrect.

## Quick Test

After configuration, click the "Sign in with Google" button. You should see:
- Google Sign In dialog appears
- After selecting an account, you're signed in
- If errors occur, they'll be displayed below the button

