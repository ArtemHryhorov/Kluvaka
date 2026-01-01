package co.kluvaka.cmp.features.auth.data.repository

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import co.kluvaka.cmp.features.auth.domain.model.AuthUser
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Composable
actual fun rememberGoogleSignInLauncher(
    onResult: (Result<AuthUser>) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()
    
    val oneTapClient = remember { Identity.getSignInClient(context) }
    
    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                
                if (idToken != null) {
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    // Sign in with Firebase
                    firebaseAuth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = task.result?.user?.toAuthUser()
                                    ?: run {
                                        onResult(Result.failure(IllegalStateException("User is null")))
                                        return@addOnCompleteListener
                                    }
                                onResult(Result.success(user))
                            } else {
                                onResult(Result.failure(task.exception ?: Exception("Sign in failed")))
                            }
                        }
                } else {
                    onResult(Result.failure(Exception("ID token is null")))
                }
            } catch (e: ApiException) {
                onResult(Result.failure(e))
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        } else {
            onResult(Result.failure(Exception("Google Sign In was cancelled")))
        }
    }
    
    return remember {
        {
            val clientId = getGoogleClientId(context)
            if (clientId == "YOUR_WEB_CLIENT_ID" || clientId.isBlank()) {
                val errorMsg = "Google Sign In is not configured. Please:\n" +
                        "1. Enable Google Sign In in Firebase Console\n" +
                        "2. Get your Web Client ID\n" +
                        "3. Set it in GoogleSignInLauncher.android.kt"
                println(errorMsg)
                onResult(Result.failure(Exception(errorMsg)))
                return@remember
            }
            
            try {
                // Start Google Sign In flow
                val signInRequest = BeginSignInRequest.builder()
                    .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                            .setSupported(true)
                            .setServerClientId(clientId)
                            .setFilterByAuthorizedAccounts(false)
                            .build()
                    )
                    .build()
                
                println("Starting Google Sign In with client ID: ${clientId.take(20)}...")
                oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener { result ->
                        try {
                            println("Google Sign In request successful, launching intent...")
                            val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                            signInLauncher.launch(intentSenderRequest)
                        } catch (e: Exception) {
                            println("Google Sign In launch error: ${e.message}")
                            e.printStackTrace()
                            onResult(Result.failure(e))
                        }
                    }
                    .addOnFailureListener { e ->
                        println("Google Sign In beginSignIn error: ${e.message}")
                        e.printStackTrace()
                        onResult(Result.failure(e))
                    }
            } catch (e: Exception) {
                println("Google Sign In setup error: ${e.message}")
                e.printStackTrace()
                onResult(Result.failure(e))
            }
        }
    }
}

private fun getGoogleClientId(context: Context): String {
    // Try to extract from google-services.json first
    val clientIdFromJson = getGoogleClientIdFromJson(context)
    if (clientIdFromJson != null) {
        return clientIdFromJson
    }
    
    // TODO: Replace with your Web Client ID from Firebase Console
    // Steps to get it:
    // 1. Firebase Console > Authentication > Sign-in method > Google
    // 2. Enable Google Sign In
    // 3. Copy the "Web client ID" from "Web SDK configuration" section
    // 4. Paste it below (it looks like: 123456789-xxxxx.apps.googleusercontent.com)
    return "799791600780-77a2nejv9engk2gtbd6gsmf6ckl9hogj.apps.googleusercontent.com" // Replace this with your actual Web Client ID
}

private fun com.google.firebase.auth.FirebaseUser.toAuthUser(): AuthUser {
    return AuthUser(
        uid = uid,
        email = email,
        displayName = displayName
    )
}

