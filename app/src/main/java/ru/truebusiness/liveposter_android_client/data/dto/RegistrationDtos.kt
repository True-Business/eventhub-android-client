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

data class UserDto(
    val id: String,
    val username: String,
    val shortId: String?,
    val bio: String?,
    val registrationDate: String?,
    val confirmed: Boolean,
    val coverUrl: String? = null
)
