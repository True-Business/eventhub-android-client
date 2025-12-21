package ru.truebusiness.liveposter_android_client.view.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.truebusiness.liveposter_android_client.repository.AuthRepository

class ProfileViewModel(
    private val authRepository: AuthRepository
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
                    _uiState.value = _uiState.value.copy(
                        name = user.username,
                        username = user.shortId?.let { "@$it" } ?: "",
                        avatarUrl = user.coverUrl ?: "",
                        about = user.bio ?: ""
                    )
                }
            }
            .launchIn(viewModelScope)
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
    val currentOrganizationIndex: Int = 0
)

data class Organization(
    val name: String,
    val membersCount: Int,
    val description: String,
    val imageUrl: String
)
