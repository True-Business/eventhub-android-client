package ru.truebusiness.liveposter_android_client.repository.api

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import ru.truebusiness.liveposter_android_client.data.dto.FriendDto

interface UserApi {

    @DELETE("api/v1/user")
    suspend fun deleteAccount(): Response<Unit>

    @GET("api/v1/friend/list/{userId}")
    suspend fun getUserFriends(@Path("userId") userId: String): Response<List<FriendDto>>
}