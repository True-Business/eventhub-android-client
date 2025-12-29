package ru.truebusiness.liveposter_android_client.data

import java.time.LocalDateTime
import java.util.UUID

data class Friend(
    val id: UUID,
    val username: String,
    val shortId: String,
    val bio: String,
    val registrationDate: LocalDateTime,
    val confirmed: Boolean
)
