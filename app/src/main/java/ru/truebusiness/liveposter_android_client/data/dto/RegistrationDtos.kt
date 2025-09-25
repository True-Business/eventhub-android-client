package ru.truebusiness.liveposter_android_client.data.dto

data class UserInfoRegistrationDto(
    val id: String,
    val username: String,
    val shortId: String,
)

data class UserCredentialsRegistrationDto(
    val email: String,
    val password: String,
)

data class RegistrationResponseDto(
    val id: String?,
    val registrationDate: String?,
    val status: String,
    val reason: String?,
)

enum class RegistrationStatus { PENDING, SUCCESS, ERROR }