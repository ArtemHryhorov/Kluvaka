package co.kluvaka.cmp.features.auth.data.repository

import android.content.Context
import org.json.JSONObject
import java.io.File

/**
 * Helper to extract Google Client ID from google-services.json
 * Note: google-services.json is processed at build time, so we can't read it at runtime.
 * This function is kept for reference but won't work. You need to configure the client ID manually.
 */
fun getGoogleClientIdFromJson(context: Context): String? {
    // Note: google-services.json is processed at build time by Google Services plugin
    // The oauth_client data is not available at runtime unless Google Sign In is configured
    // and the file is properly updated by Firebase.
    return null
}

