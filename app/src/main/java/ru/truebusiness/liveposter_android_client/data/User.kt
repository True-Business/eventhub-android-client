package ru.truebusiness.liveposter_android_client.data

import ru.truebusiness.liveposter_android_client.data.dto.UserDto

data class User(
    val id: String,
    val username: String,
    val shortId: String?,
    val bio: String?,
    val registrationDate: String?,
    val confirmed: Boolean,
    val coverUrl: String? = null
) {
    // Алиас для совместимости со старым кодом
    val userName: String get() = username

    override fun toString(): String = """
        id = $id,
        username = $username,
        shortId = $shortId
    """.trimIndent()
}

fun UserDto.toUser(): User = User(
    id = id,
    username = username,
    shortId = shortId,
    bio = bio,
    registrationDate = registrationDate,
    confirmed = confirmed,
    coverUrl = coverUrl
)