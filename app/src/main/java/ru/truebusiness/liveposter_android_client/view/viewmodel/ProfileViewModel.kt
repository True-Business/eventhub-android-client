package ru.truebusiness.liveposter_android_client.view.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ru.truebusiness.liveposter_android_client.R

class ProfileViewModel : ViewModel() {
    private val TAG = "PROFILE_VIEW_MODEL"
    private val _uiState = mutableStateOf(
        ProfileUiState(
            name = "Василий Попов",
            username = "@vasily_P",
            avatarUrl = "https://i.pinimg.com/236x/c6/00/f2/c600f276b3f7cafcd572402ac86e489b.jpg",
            about = "Я Вася Пупкин, студент 2 курса ФИТ НГУ и вот какой я классный. Приходите все на мероприятия НГУ!",
            eventsCreated = 5,
            eventsVisited = 15,
            organizations = listOf(
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
        )
    )
    val uiState = _uiState

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
