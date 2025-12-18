package ru.truebusiness.liveposter_android_client.view.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State

class ProfileSettingsViewModel : ViewModel() {
    private val TAG = "PROFILE_SETTINGS_VIEW_MODEL"

    private val _uiState = mutableStateOf(
        ProfileSettingsUiState(
            name = "Василий Попов",
            username = "@vasily_P",
            avatarUrl = "https://i.pinimg.com/236x/c6/00/f2/c600f276b3f7cafcd572402ac86e489b.jpg"
        )
    )
    val uiState: State<ProfileSettingsUiState> = _uiState

    fun logout() {
        // мок: выход из аккаунта
        println("Logout pressed")
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
