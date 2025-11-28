package com.example.farmacia_medicitas.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farmacia_medicitas.data.auth.AuthRepository
import com.example.farmacia_medicitas.data.remote.dto.auth.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: UserDto? = null,
    val isAuthenticated: Boolean = false,
    val sessionCheckMessage: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AuthState(isAuthenticated = repo.getAccess() != null))
    val state: StateFlow<AuthState> = _state

    fun login(emailOrUsername: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            runCatching {
                repo.login(emailOrUsername, password)
            }.onSuccess { res ->
                _state.value = AuthState(isLoading = false, error = null, user = res.user, isAuthenticated = true)
                onSuccess()
            }.onFailure { e ->
                _state.value = AuthState(isLoading = false, error = e.localizedMessage ?: "Error de inicio de sesión", user = null, isAuthenticated = false)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
            _state.value = AuthState(isAuthenticated = false)
        }
    }

    fun checkSession() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, sessionCheckMessage = null)
            runCatching {
                repo.me()
            }.onSuccess { me ->
                _state.value = _state.value.copy(isLoading = false, user = me, isAuthenticated = true, sessionCheckMessage = "Sesión OK")
            }.onFailure { e ->
                _state.value = _state.value.copy(isLoading = false, error = e.localizedMessage ?: "Error al verificar sesión", sessionCheckMessage = "Fallo: ${e.localizedMessage}")
            }
        }
    }

    fun invalidateAccessAndCheckForDebug() {
        viewModelScope.launch {
            repo.invalidateAccessForTest()
            checkSession()
        }
    }
}