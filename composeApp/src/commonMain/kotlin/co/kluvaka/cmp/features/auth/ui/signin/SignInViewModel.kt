package co.kluvaka.cmp.features.auth.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.auth.domain.repository.AuthRepository
import co.kluvaka.cmp.features.auth.domain.usecase.LoginUser
import co.kluvaka.cmp.features.auth.domain.usecase.LogOutUser
import co.kluvaka.cmp.features.auth.domain.usecase.SignInWithGoogle
import co.kluvaka.cmp.features.auth.domain.usecase.SignUpUser
import co.kluvaka.cmp.features.auth.ui.signin.SignInOperation.Actions
import co.kluvaka.cmp.features.auth.ui.signin.SignInOperation.Events
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(
    private val loginUser: LoginUser,
    private val signUpUser: SignUpUser,
    private val signInWithGoogle: SignInWithGoogle,
    private val logOutUser: LogOutUser,
    private val authRepository: AuthRepository,
) : ViewModel() {
    
    private val _mutableState = MutableStateFlow(SignInState())
    val state: StateFlow<SignInState> = _mutableState
    
    val authState = authRepository.observeAuthState()
    
    fun handleAction(action: Actions) {
        when (action) {
            is Actions.EmailUpdate -> {
                _mutableState.update { it.copy(email = action.email, errorMessage = null) }
            }
            is Actions.PasswordUpdate -> {
                _mutableState.update { it.copy(password = action.password, errorMessage = null) }
            }
            is Actions.Login -> handleLogin()
            is Actions.SignUp -> handleSignUp()
            is Actions.SignInWithGoogle -> handleGoogleSignIn()
            is Actions.SetGoogleSignInError -> {
                _mutableState.update { it.copy(googleSignInError = action.error) }
            }
            is Actions.LogOut -> handleLogOut()
            is Actions.ClearError -> {
                _mutableState.update { it.copy(errorMessage = null, googleSignInError = null) }
            }
        }
    }
    
    private fun handleLogin() {
        val currentState = _mutableState.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _mutableState.update { it.copy(errorMessage = "Email and password are required") }
            return
        }
        
        _mutableState.update { it.copy(isLoading = true, errorMessage = null) }
        
        viewModelScope.launch {
            loginUser(currentState.email, currentState.password)
                .onSuccess { user ->
                    _mutableState.update { it.copy(isLoading = false) }
                    dispatchEvent(Events.LoginSuccess(user))
                }
                .onFailure { error ->
                    _mutableState.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Login failed"
                        )
                    }
                    dispatchEvent(Events.LoginFailure(error))
                }
        }
    }
    
    private fun handleSignUp() {
        val currentState = _mutableState.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _mutableState.update { it.copy(errorMessage = "Email and password are required") }
            return
        }
        
        _mutableState.update { it.copy(isLoading = true, errorMessage = null) }
        
        viewModelScope.launch {
            signUpUser(currentState.email, currentState.password)
                .onSuccess { user ->
                    _mutableState.update { it.copy(isLoading = false) }
                    dispatchEvent(Events.SignUpSuccess(user))
                }
                .onFailure { error ->
                    _mutableState.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Sign up failed"
                        )
                    }
                    dispatchEvent(Events.SignUpFailure(error))
                }
        }
    }
    
    private fun handleGoogleSignIn() {
        _mutableState.update { it.copy(isLoading = true, errorMessage = null) }
        
        viewModelScope.launch {
            signInWithGoogle()
                .onSuccess { user ->
                    _mutableState.update { it.copy(isLoading = false) }
                    dispatchEvent(Events.GoogleSignInSuccess(user))
                }
                .onFailure { error ->
                    _mutableState.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Google Sign In failed"
                        )
                    }
                    dispatchEvent(Events.GoogleSignInFailure(error))
                }
        }
    }
    
    private fun handleLogOut() {
        _mutableState.update { it.copy(isLoading = true, errorMessage = null) }
        
        viewModelScope.launch {
            logOutUser()
                .onSuccess {
                    _mutableState.update { it.copy(isLoading = false) }
                    dispatchEvent(Events.LogOutSuccess)
                }
                .onFailure { error ->
                    _mutableState.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Logout failed"
                        )
                    }
                    dispatchEvent(Events.LogOutFailure(error))
                }
        }
    }
    
    private fun dispatchEvent(event: Events) {
        // Events can be handled here if needed for side effects
        // For now, state updates are handled in the action handlers
    }
}

