package ru.truebusiness.liveposter_android_client.repository.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.truebusiness.liveposter_android_client.data.dto.EventDto
import ru.truebusiness.liveposter_android_client.data.dto.EventCreateUpdateDto

interface EventApi {

    @GET("api/v1/event")
    fun getEvents(@Query("category") category: String? = null): Call<List<EventDto>>

    @GET("api/v1/event/{eventID}")
    fun getEvent(@Path("eventID") eventId: String): Call<EventDto>

    @POST("api/v1/event")
    fun createEvent(@Body event: EventCreateUpdateDto): Call<EventDto>

    @PUT("api/v1/event/{eventID}")
    fun updateEvent(@Path("eventID") eventId: String, @Body event: EventCreateUpdateDto): Call<EventDto>

    @DELETE("api/v1/event/{eventID}/draft")
    fun deleteEvent(@Path("eventID") eventId: String): Call<Void>
}