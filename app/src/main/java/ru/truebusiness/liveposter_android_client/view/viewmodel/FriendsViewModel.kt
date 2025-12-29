package ru.truebusiness.liveposter_android_client.view.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.truebusiness.liveposter_android_client.data.Friend
import ru.truebusiness.liveposter_android_client.repository.AuthRepository
import ru.truebusiness.liveposter_android_client.repository.UserRepository
import java.util.UUID

class FriendsViewModel(
    authRepository: AuthRepository
) : ViewModel() {
    private val TAG = "FRIENDS_VIEW_MODEL"

    private val userState = mutableStateOf(
        User(
            id = null,
            username = "",
            photoURL = ""
        )
    )
    var friendsList by mutableStateOf<List<Friend>>(emptyList())
        private set

    init {
        // Подписываемся на изменения данных пользователя из DataStore (источник правды)
        authRepository.currentUser
            .onEach { user ->
                if (user != null) {
                    userState.value = userState.value.copy(
                        id = UUID.fromString(user.id),
                        username = user.username,
                        photoURL = user.photoUrl ?: ""
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private val repository = UserRepository()

    val currentUser = authRepository.currentUser

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    // 0 - Все, 1 - Входящие, 2 - Исходящие
    var selectedTab by mutableStateOf(0)
        private set

    fun loadFriends() {
        val userId = userState.value.id ?: return
        viewModelScope.launch {
            repository.getFriends(userId)?.let { friends ->
                friendsList = friends
            }
        }
    }

    fun onTabSelected(tabIndex: Int) {
        selectedTab = tabIndex
    }

    fun onSearchQueryChanged(query: String) {
        if (query.length <= 128) {
            _searchQuery.value = query
        }
    }
}

data class User(
    val id: UUID? = null,
    val username: String,
    val photoURL: String
)