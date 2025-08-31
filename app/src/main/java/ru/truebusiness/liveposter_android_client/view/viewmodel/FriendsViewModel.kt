package ru.truebusiness.liveposter_android_client.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.truebusiness.liveposter_android_client.data.Friend
import ru.truebusiness.liveposter_android_client.repository.UserRepository
import java.util.UUID

class FriendsViewModel: ViewModel() {
    private val TAG = "FRIENDS_VIEW_MODEL"

    private val repository = UserRepository()

    private val _friends = MutableLiveData<List<Friend>>(emptyList())
    val friends: LiveData<List<Friend>> = _friends

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    // 0 - Все, 1 - Входящие, 2 - Исходящие
    private val _selectedTab = MutableLiveData(0)
    val selectedTab: LiveData<Int> = _selectedTab

    fun loadFriends(userId: UUID) {
        repository.getUserMockFriends(userId) { friends ->
            _friends.value = friends ?: emptyList()
        }
    }

    fun onTabSelected(tabIndex: Int) {
        _selectedTab.value = tabIndex
    }

    fun onSearchQueryChanged(query: String) {
        if (query.length <= 128) {
            _searchQuery.value = query
        }
    }
}