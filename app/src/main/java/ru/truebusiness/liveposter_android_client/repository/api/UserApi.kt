package ru.truebusiness.liveposter_android_client.repository.api

import retrofit2.http.GET

interface UserApi {
    @GET("api/v1/user")
    fun getUserFriends()

}