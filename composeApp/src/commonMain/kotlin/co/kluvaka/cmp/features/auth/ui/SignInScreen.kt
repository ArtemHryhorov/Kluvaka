package co.kluvaka.cmp.features.auth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.auth.data.repository.rememberGoogleSignInLauncher
import co.kluvaka.cmp.features.auth.ui.signin.SignInOperation.Actions
import co.kluvaka.cmp.features.sessions.ui.sessions.SessionsScreen
import org.koin.compose.viewmodel.koinViewModel

object SignInScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = koinViewModel<co.kluvaka.cmp.features.auth.ui.signin.SignInViewModel>()
        val state by viewModel.state.collectAsState()
        val authState by viewModel.authState.collectAsState(initial = null)
        
        // Navigate to sessions if user is authenticated
        LaunchedEffect(authState) {
            if (authState != null) {
                navigator?.popUntilRoot()
                navigator?.replace(SessionsScreen)
            }
        }
        
        val googleSignInLauncher = rememberGoogleSignInLauncher { result ->
            result.onSuccess { user ->
                // Google Sign In successful - authState will update automatically
                viewModel.handleAction(Actions.SetGoogleSignInError(null))
            }.onFailure { error ->
                // Display error to user
                val errorMessage = error.message ?: "Google Sign In failed"
                println("Google Sign In Error: $errorMessage")
                error.printStackTrace()
                viewModel.handleAction(Actions.SetGoogleSignInError(errorMessage))
            }
        }
        
        SignInScreenContent(
            state = state,
            onEmailChange = { viewModel.handleAction(Actions.EmailUpdate(it)) },
            onPasswordChange = { viewModel.handleAction(Actions.PasswordUpdate(it)) },
            onLoginClick = { viewModel.handleAction(Actions.Login) },
            onSignUpClick = { viewModel.handleAction(Actions.SignUp) },
            onGoogleSignInClick = { googleSignInLauncher() },
            onLogOutClick = { viewModel.handleAction(Actions.LogOut) },
            onClearError = { viewModel.handleAction(Actions.ClearError) },
            isAuthenticated = authState != null,
            userEmail = authState?.email,
        )
    }
}

@Composable
private fun SignInScreenContent(
    state: co.kluvaka.cmp.features.auth.ui.signin.SignInState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onGoogleSignInClick: () -> Unit,
    onLogOutClick: () -> Unit,
    onClearError: () -> Unit,
    isAuthenticated: Boolean,
    userEmail: String?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isAuthenticated) {
            Text("Welcome, ${userEmail ?: "User"}!")
            Button(
                onClick = onLogOutClick,
                enabled = !state.isLoading
            ) {
                Text(if (state.isLoading) "Signing out..." else "Sign Out")
            }
        } else {
            OutlinedTextField(
                value = state.email,
                onValueChange = onEmailChange,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )
            
            state.errorMessage?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            state.googleSignInError?.let {
                Text(
                    text = "Google Sign In: $it",
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            Button(
                onClick = onLoginClick,
                enabled = !state.isLoading && state.email.isNotBlank() && state.password.isNotBlank()
            ) {
                Text(if (state.isLoading) "Signing in..." else "Sign In")
            }
            
            Button(
                onClick = onSignUpClick,
                enabled = !state.isLoading && state.email.isNotBlank() && state.password.isNotBlank(),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(if (state.isLoading) "Signing up..." else "Sign Up")
            }
            
            Button(
                onClick = onGoogleSignInClick,
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Sign in with Google")
            }
        }
    }
}
