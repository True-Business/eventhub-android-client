package ru.truebusiness.liveposter_android_client.data.dto

data class FriendDto(
    val id: String,
    val username: String,
    val shortId: String,
    val bio: String,
    val registrationDate: String,
    val confirmed: Boolean
)