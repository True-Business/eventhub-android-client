package ru.truebusiness.liveposter_android_client.data

import java.util.UUID

data class Organization(
    val id: UUID,
    val name: String,
    val coverUrl: String,
    val address: String,
    val description: String,
    val admins: List<User>,
    val events: List<Event>,
    val images: List<String>
)