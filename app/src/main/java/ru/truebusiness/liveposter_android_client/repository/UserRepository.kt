package ru.truebusiness.liveposter_android_client.repository

import ru.truebusiness.liveposter_android_client.data.Friend
import ru.truebusiness.liveposter_android_client.repository.mocks.mockUserFriendsList
import java.util.UUID

class UserRepository {
    fun getUserMockFriends(userId: UUID, onResult: (List<Friend>?) -> Unit) {
        onResult(mockUserFriendsList)
    }
}