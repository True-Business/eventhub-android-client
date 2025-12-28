package ru.truebusiness.liveposter_android_client.view.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.truebusiness.liveposter_android_client.data.ImageOwner
import ru.truebusiness.liveposter_android_client.repository.AuthRepository
import ru.truebusiness.liveposter_android_client.repository.StorageRepository

class ProfileSettingsViewModel(
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository
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
                        avatarUrl = user.photoUrl ?: ""
                    )
                    // Если фото ещё не загружено в DataStore, загружаем из Storage API
                    if (user.photoUrl == null) {
                        loadUserPhoto(user.id)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Загружает текущее фото пользователя из Storage API.
     * Выбирается самое свежее фото по дате загрузки.
     * После успешной загрузки сохраняет URL в AuthRepository (источник правды).
     */
    private fun loadUserPhoto(userId: String) {
        viewModelScope.launch {
            val owner = ImageOwner.User(userId)
            val result = storageRepository.getCoverImageUrl(owner)
            result.onSuccess { url ->
                _uiState.value = _uiState.value.copy(avatarUrl = url ?: "")
                // Сохраняем в источник правды
                authRepository.updatePhotoUrl(url)
            }.onFailure {
                // Если ошибка - оставляем пустой URL, будет показана заглушка
                _uiState.value = _uiState.value.copy(avatarUrl = "")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    /**
     * Показывает диалог подтверждения удаления аккаунта.
     */
    fun showDeleteConfirmation() {
        _uiState.value = _uiState.value.copy(showDeleteConfirmDialog = true)
    }

    /**
     * Скрывает диалог подтверждения удаления аккаунта.
     */
    fun hideDeleteConfirmation() {
        _uiState.value = _uiState.value.copy(showDeleteConfirmDialog = false)
    }

    /**
     * Сбрасывает ошибку удаления аккаунта.
     */
    fun clearDeleteError() {
        _uiState.value = _uiState.value.copy(deleteError = null)
    }

    /**
     * Удаляет аккаунт пользователя.
     * После успешного удаления DataStore очищается и происходит автоматический logout.
     */
    fun deleteAccount() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isDeleting = true,
                showDeleteConfirmDialog = false,
                deleteError = null
            )

            val result = authRepository.deleteAccount()

            result.onSuccess {
                // После успешного удаления logout произойдёт автоматически
                // (DataStore очищен, isLoggedIn станет null/false)
                _uiState.value = _uiState.value.copy(isDeleting = false)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isDeleting = false,
                    deleteError = e.message ?: "Ошибка удаления аккаунта"
                )
            }
        }
    }
}

data class ProfileSettingsUiState(
    val name: String,
    val username: String,
    val avatarUrl: String,
    val showDeleteConfirmDialog: Boolean = false,
    val isDeleting: Boolean = false,
    val deleteError: String? = null
)
