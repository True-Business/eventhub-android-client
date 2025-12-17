package ru.truebusiness.liveposter_android_client.view.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.truebusiness.liveposter_android_client.repository.AuthRepository

class ProfileSettingsViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(
        ProfileSettingsUiState(
            name = "",
            username = "",
            avatarUrl = ""
        )
    )
    val uiState: State<ProfileSettingsUiState> = _uiState

    init {
        // Подписываемся на изменения данных пользователя из DataStore (источник правды)
        authRepository.currentUser
            .onEach { user ->
                if (user != null) {
                    _uiState.value = _uiState.value.copy(
                        name = user.username,
                        username = user.shortId?.let { "@$it" } ?: "",
                        avatarUrl = user.coverUrl ?: ""
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun deleteAccount() {
        // мок: удаление аккаунта
        println("Delete account pressed")
    }
}

data class ProfileSettingsUiState(
    val name: String,
    val username: String,
    val avatarUrl: String,
)
