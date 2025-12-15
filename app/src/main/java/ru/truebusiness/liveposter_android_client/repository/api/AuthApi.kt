package ru.truebusiness.liveposter_android_client.repository.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.truebusiness.liveposter_android_client.data.dto.RegistrationResponseDto
import ru.truebusiness.liveposter_android_client.data.dto.UserCredentialsRegistrationDto
import ru.truebusiness.liveposter_android_client.data.dto.UserDto
import ru.truebusiness.liveposter_android_client.data.dto.UserInfoRegistrationDto

//TODO: Вынести в какой-нибудь конфиг?
private const val TARGET = "/prod"
interface AuthApi {

    /**
     * TODO: Для бекенда - требуется реализовать endpoint логина
     *
     * POST /api/v1/auth/login
     *
     * Запрос:
     * - Использует Basic Auth (email:password в заголовке Authorization)
     * - Body: UserCredentialsRegistrationDto { email: String, password: String }
     *
     * Ответ при успехе (200):
     * - UserDto { id, username (=email), shortId, bio, registrationDate, confirmed }
     *
     * Ответ при ошибке:
     * - 401 Unauthorized: неверные креды -> ErrorResponseDto { code: 401, message: "Invalid credentials" }
     * - 404 Not Found: пользователь не найден -> ErrorResponseDto { code: 404, message: "User not found" }
     */
    @POST("$TARGET/api/v1/auth/login")
    suspend fun login(@Body dto: UserCredentialsRegistrationDto): UserDto

    @POST("$TARGET/api/v1/auth")
    suspend fun preRegister(@Body dto: UserCredentialsRegistrationDto): RegistrationResponseDto

    @PUT("$TARGET/api/v1/auth/send-code/{userId}")
    suspend fun sendConfirmationCode(@Path("userId") userId: String): Response<Unit>

    @PUT("$TARGET/api/v1/auth/check-code/{code}")
    suspend fun verifyConfirmationCode(@Path("code") code: String): RegistrationResponseDto

    @POST("$TARGET/api/v1/auth/add-info")
    suspend fun postRegister(@Body dto: UserInfoRegistrationDto): RegistrationResponseDto
}