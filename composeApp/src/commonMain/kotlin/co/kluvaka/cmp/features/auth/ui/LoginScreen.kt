package co.kluvaka.cmp.features.auth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.kluvaka.cmp.features.auth.domain.repository.AuthRepository
import co.kluvaka.cmp.features.auth.domain.usecase.LogOutUser
import co.kluvaka.cmp.features.auth.domain.usecase.LoginUser
import co.kluvaka.cmp.features.auth.domain.usecase.SignUpUser
import org.koin.compose.koinInject

@Composable
fun LoginScreen(
    loginUser: LoginUser = koinInject(),
    signUpUser: SignUpUser = koinInject(),
    logOutUser: LogOutUser = koinInject(),
    authRepository: AuthRepository = koinInject(),
) {
    val authState by authRepository.observeAuthState().collectAsState(initial = null)
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (authState != null) {
            Text("Welcome, ${authState?.email ?: "User"}!")
            Button(onClick = {
                isLoading = true
                // Sign out logic
            }) {
                Text(if (isLoading) "Signing out..." else "Sign Out")
            }
        } else {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxSize()
            )
            
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxSize()
            )
            
            errorMessage?.let {
                Text(it)
            }
            
            Button(
                onClick = {
                    isLoading = true
                    // Sign in logic - use loginUser(email, password)
                },
                enabled = !isLoading
            ) {
                Text(if (isLoading) "Signing in..." else "Sign In")
            }
        }
    }
}

