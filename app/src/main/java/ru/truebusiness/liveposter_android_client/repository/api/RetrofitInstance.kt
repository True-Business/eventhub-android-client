package ru.truebusiness.liveposter_android_client.repository.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.Base64
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object RetrofitInstance {
    private const val BASE_URL = "http://eventhub-backend.ru/dev/"
    
    // TODO: Убрать fallback креды после завершения разработки
    private const val FALLBACK_USERNAME = "user1@example.com"
    private const val FALLBACK_PASSWORD = "secure_password123"

    // Провайдер учетных данных для Basic Auth (инициализируется в MainActivity)
    private var credentialsProvider: CredentialsProvider? = null

    /**
     * Инициализирует RetrofitInstance с провайдером учетных данных.
     * Должен быть вызван в MainActivity перед использованием API.
     */
    fun initialize(provider: CredentialsProvider) {
        credentialsProvider = provider
    }

    private val authClient by lazy {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(logger)
            .addInterceptor { chain ->
                // Получаем креды динамически при каждом запросе
                val (email, password) = credentialsProvider?.getCredentials()
                    ?: Pair(FALLBACK_USERNAME, FALLBACK_PASSWORD)


                //val credentials = "$email:$password"
                val credentials = "user1@example.com:secure_password123"
                val basicAuth = "Basic " + Base64.getEncoder().encodeToString(credentials.toByteArray())

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
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
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

    /**
     * TrustManager, который доверяет всем сертификатам (только для dev/test).
     */
    private val trustAllCerts: Array<TrustManager> = arrayOf(
        object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        }
    )

    /**
     * Insecure OkHttpClient для загрузки файлов на presigned URLs.
     * Игнорирует SSL-сертификаты (для self-signed сертификатов в dev-окружении).
     * Увеличенные таймауты для загрузки больших файлов.
     */
    val insecureUploadClient: OkHttpClient by lazy {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCerts, SecureRandom())

        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .addInterceptor(logger)
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
    val storageApi: StorageApi by lazy { authRetrofit.create(StorageApi::class.java) }

    // API без аутентификации
    val authApi: AuthApi by lazy { publicRetrofit.create(AuthApi::class.java) }
}