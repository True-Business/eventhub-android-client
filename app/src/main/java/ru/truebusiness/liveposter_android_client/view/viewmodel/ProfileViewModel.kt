package ru.truebusiness.liveposter_android_client.view.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.truebusiness.liveposter_android_client.data.ImageOwner
import ru.truebusiness.liveposter_android_client.repository.AuthRepository
import ru.truebusiness.liveposter_android_client.repository.StorageRepository
import java.io.File
import java.io.FileOutputStream

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {

    // Моковые данные для организаций и статистики (пока нет API)
    private val mockEventsCreated = 5
    private val mockEventsVisited = 15
    private val mockOrganizations = listOf(
        Organization(
            name = "НГУ",
            membersCount = 1400,
            description = "НГУ - классический университет в Новосибирске, известный своей тесной интеграцией с научными институтами Сибирского отделения РАН. Университет предлагает высшее образование на 6 факультетах и в 4 институтах, сочетая естественнонаучные, инженерные и гуманитарные направления.",
            imageUrl = "https://nadvizh.ru/media/events_img/67/smart-piknik_logo.jpg"
        ),
        Organization(
            name = "IT Клуб",
            membersCount = 230,
            description = "Клуб, объединяющий студентов и выпускников, увлечённых IT-проектами и разработкой.",
            imageUrl = "https://nadvizh.ru/media/events_img/67/smart-piknik_logo.jpg"
        )
    )

    private var currentUserId: String? = null

    private val _uiState = mutableStateOf(
        ProfileUiState(
            name = "",
            username = "",
            avatarUrl = "",
            about = "",
            eventsCreated = mockEventsCreated,
            eventsVisited = mockEventsVisited,
            organizations = mockOrganizations
        )
    )
    val uiState = _uiState

    init {
        // Подписываемся на изменения данных пользователя из DataStore (источник правды)
        authRepository.currentUser
            .onEach { user ->
                if (user != null) {
                    currentUserId = user.id
                    _uiState.value = _uiState.value.copy(
                        name = user.username,
                        username = user.shortId?.let { "@$it" } ?: "",
                        about = user.bio ?: "",
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

    /**
     * Загружает новое фото пользователя в Storage API.
     * После успешной загрузки локально обновляет URL фото.
     */
    fun uploadUserPhoto(context: Context, uri: Uri) {
        val userId = currentUserId ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUploadingPhoto = true, photoUploadError = null)

            try {
                val file = uriToFile(context, uri)
                if (file == null) {
                    _uiState.value = _uiState.value.copy(
                        isUploadingPhoto = false,
                        photoUploadError = "Не удалось прочитать файл"
                    )
                    return@launch
                }

                val owner = ImageOwner.User(userId)
                val result = storageRepository.uploadCoverImage(owner, file)

                result.onSuccess {
                    // После успешной загрузки получаем новый URL фото
                    loadUserPhoto(userId)
                    _uiState.value = _uiState.value.copy(isUploadingPhoto = false)
                }.onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isUploadingPhoto = false,
                        photoUploadError = e.message ?: "Ошибка загрузки фото"
                    )
                }

                // Удаляем временный файл
                file.delete()

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isUploadingPhoto = false,
                    photoUploadError = e.message ?: "Ошибка загрузки фото"
                )
            }
        }
    }

    /**
     * Сбрасывает ошибку загрузки фото.
     */
    fun clearPhotoUploadError() {
        _uiState.value = _uiState.value.copy(photoUploadError = null)
    }

    /**
     * Конвертирует Uri в File, копируя содержимое во временный файл.
     */
    private fun uriToFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val extension = context.contentResolver.getType(uri)?.substringAfter("/") ?: "jpg"
            val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}.$extension")
            FileOutputStream(file).use { output -> inputStream.copyTo(output) }
            inputStream.close()
            file
        } catch (e: Exception) {
            null
        }
    }

    fun updateProfile(username: String? = null, bio: String? = null) {
        viewModelScope.launch {
            authRepository.updateUserProfile(username = username, bio = bio)
        }
    }

    fun nextOrganization() {
        val list = _uiState.value.organizations
        if (list.size > 1) {
            _uiState.value = _uiState.value.copy(
                currentOrganizationIndex = (_uiState.value.currentOrganizationIndex + 1) % list.size
            )
        }
    }

    fun prevOrganization() {
        val list = _uiState.value.organizations
        if (list.size > 1) {
            _uiState.value = _uiState.value.copy(
                currentOrganizationIndex = (_uiState.value.currentOrganizationIndex - 1 + list.size) % list.size
            )
        }
    }
}

data class ProfileUiState(
    val name: String,
    val username: String,
    val avatarUrl: String,
    val about: String,
    val eventsCreated: Int,
    val eventsVisited: Int,
    val organizations: List<Organization>,
    val currentOrganizationIndex: Int = 0,
    val isUploadingPhoto: Boolean = false,
    val photoUploadError: String? = null
)

data class Organization(
    val name: String,
    val membersCount: Int,
    val description: String,
    val imageUrl: String
)
