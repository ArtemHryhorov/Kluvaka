package co.kluvaka.cmp.features.auth.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kluvaka.cmp.features.auth.domain.repository.AuthRepository
import co.kluvaka.cmp.features.auth.domain.usecase.LogOutUser
import co.kluvaka.cmp.features.auth.ui.profile.ProfileOperation.Actions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val logOutUser: LogOutUser,
    private val authRepository: AuthRepository,
) : ViewModel() {
    
    private val _mutableState = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _mutableState
    
    init {
        loadProfile()
    }
    
    fun handleAction(action: Actions) {
        when (action) {
            is Actions.Logout -> handleLogout()
            is Actions.LoadProfile -> loadProfile()
        }
    }
    
    private fun loadProfile() {
        val currentUser = authRepository.currentUser
        _mutableState.update {
            it.copy(
                userId = currentUser?.uid,
                email = currentUser?.email,
            )
        }
        
        // Load auth token asynchronously
        viewModelScope.launch {
            authRepository.getIdToken()
                .onSuccess { token ->
                    _mutableState.update { it.copy(authToken = token) }
                }
                .onFailure {
                    _mutableState.update { it.copy(authToken = null) }
                }
        }
    }
    
    private fun handleLogout() {
        _mutableState.update { it.copy(isLoading = true) }
        
        viewModelScope.launch {
            logOutUser()
                .onSuccess {
                    _mutableState.update { it.copy(isLoading = false) }
                }
                .onFailure { error ->
                    _mutableState.update { 
                        it.copy(
                            isLoading = false,
                            // Error handling can be added here if needed
                        )
                    }
                }
        }
    }
}

