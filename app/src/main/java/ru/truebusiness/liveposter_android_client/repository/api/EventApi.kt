package ru.truebusiness.liveposter_android_client.repository.api

import retrofit2.Call
import retrofit2.http.GET
import ru.truebusiness.liveposter_android_client.data.Event

interface EventApi {

    @GET("events")
    fun getEvents(): Call<List<Event>>
}