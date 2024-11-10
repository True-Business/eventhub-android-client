package ru.truebusiness.liveposter_android_client.repository.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.truebusiness.liveposter_android_client.data.Event

interface EventApi {

    @GET("events")
    fun getEvents(@Query("category") category: String? = null): Call<List<Event>>

    @GET("events/{id}")
    fun getEvent(@Path("id") id: String): Call<Event>
}