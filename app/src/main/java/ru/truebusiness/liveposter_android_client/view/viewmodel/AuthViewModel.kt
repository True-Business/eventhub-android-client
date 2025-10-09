package ru.truebusiness.liveposter_android_client.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.truebusiness.liveposter_android_client.data.dto.RegistrationResponseDto
import ru.truebusiness.liveposter_android_client.repository.AuthRepository


data class AuthState(
    val loading: Boolean = false,
    val error: String? = null,
    val response: RegistrationResponseDto? = null,

    val currentUserId: String? = null,

    val email: String? = null,
    val username: String? = null,
    val password: String? = null,
    val shortId: String? = null,
    val isLoggedIn: Boolean = false,
)

class AuthViewModel(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()
    val isLoggedIn = authRepository.isLoggedIn.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val email = authRepository.email.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun preRegister(
        email: String,
        password: String,
        onPending: (String?) -> Unit = {}
    ) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(loading = true, error = null, email = email, password = password))
            try {
                val response = authRepository.preRegister(email, password)
                _state.emit(_state.value.copy(loading = false, response = response, currentUserId = response.id))
                onPending(response.id)
            } catch (e: Exception) {
                _state.emit(_state.value.copy(loading = false, error = e.message))
            }
        }
    }

    fun sendCode(userId: String) {
        viewModelScope.launch {
            try { authRepository.sendCode(userId) } catch (_: Exception) {}
        }
    }

    fun verifyCode(
        code: String,
        onSuccess: (String?) -> Unit = {}
    ) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(loading = true, error = null))
            try {
                val res = authRepository.verifyCode(code)
                _state.emit(_state.value.copy(loading = false, response = res, currentUserId = res.id))
                onSuccess(res.id)
            } catch (e: Exception) {
                _state.emit(_state.value.copy(loading = false, error = e.message))
            }
        }
    }

    fun postRegister(
        id: String,
        username: String,
        shortId: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(loading = true, error = null, username = username, shortId = shortId))
            try {
                val s = _state.value
                val res = authRepository.postRegister(
                    id = id,
                    username = username,
                    shortId = shortId,
                    email = s.email,
                    password = s.password
                )
                _state.emit(_state.value.copy(loading = false, response = res, currentUserId = res.id, isLoggedIn = true))
                onSuccess()
            } catch (e: Exception) {
                _state.emit(_state.value.copy(loading = false, error = e.message))
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.saveCredentials(email, password)
        }

    }

    fun logout() {
        viewModelScope.launch { authRepository.logout() }
    }
}