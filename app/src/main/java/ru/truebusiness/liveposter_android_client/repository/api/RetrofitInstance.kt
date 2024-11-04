package ru.truebusiness.liveposter_android_client.repository.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://example.com"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val eventApi: EventApi by lazy {
        retrofit.create(EventApi::class.java)
    }
}