package ru.truebusiness.liveposter_android_client.repository.api

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET

interface UserApi {
    @GET("api/v1/user")
    fun getUserFriends()

    @DELETE("api/v1/user")
    suspend fun deleteAccount(): Response<Unit>
}