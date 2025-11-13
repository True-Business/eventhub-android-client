package ru.truebusiness.liveposter_android_client.data

import java.util.UUID

data class EventPost(
    val id: UUID = UUID.randomUUID(),
    val publishedAt: String,
    val description: String,
    val images: List<String> = emptyList()
)