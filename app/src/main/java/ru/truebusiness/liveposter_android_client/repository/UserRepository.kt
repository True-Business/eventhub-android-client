package ru.truebusiness.liveposter_android_client.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import ru.truebusiness.liveposter_android_client.data.Friend
import ru.truebusiness.liveposter_android_client.data.dto.toDomainFriend
import ru.truebusiness.liveposter_android_client.repository.api.RetrofitInstance.userApi
import java.util.UUID

class UserRepository {
    suspend fun getFriends(userId: UUID): List<Friend>? {
        return try {
            val response = userApi.getUserFriends(userId.toString())
            if (response.isSuccessful) {
                response.body()?.let { dtos ->
                    coroutineScope {
                        dtos.map { async { it.toDomainFriend() } }.awaitAll()
                    }
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}