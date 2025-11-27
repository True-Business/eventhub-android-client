package ru.truebusiness.liveposter_android_client.repository.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Base64
import kotlin.getValue

object RetrofitInstance {
    private const val BASE_URL = "http://eventhub-backend.ru/dev/"
    
    // Учетные данные для Basic Auth (для разработки)
    private const val USERNAME = "user1@example.com"
    private const val PASSWORD = "secure_password123"

    private val authClient by lazy {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        
        // Создаем Basic Auth header
        val credentials = "$USERNAME:$PASSWORD"
        val basicAuth = "Basic " + Base64.getEncoder().encodeToString(credentials.toByteArray())
        
        OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val requestBuilder = originalRequest.newBuilder()
                    .header("Authorization", basicAuth)
                    .header("Content-Type", "application/json")
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()
    }
    
    // Клиент без аутентификации (для эндпоинтов, которые не требуют Basic Auth)
    private val publicClient by lazy {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val requestBuilder = originalRequest.newBuilder()
                    .header("Content-Type", "application/json")
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()
    }

    private val authRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(authClient)
            .build()
    }
    
    private val publicRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(publicClient)
            .build()
    }

    // API с аутентификацией
    val eventApi: EventApi by lazy { authRetrofit.create(EventApi::class.java) }
    val organizationApi: OrganizationApi by lazy { authRetrofit.create(OrganizationApi::class.java) }
    val userApi: UserApi by lazy { authRetrofit.create(UserApi::class.java) }
    
    // API без аутентификации
    val authApi: AuthApi by lazy { publicRetrofit.create(AuthApi::class.java) }
}