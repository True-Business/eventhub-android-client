package ru.truebusiness.liveposter_android_client.data

import java.util.UUID

data class Event(
    val id: UUID,
    val category: List<EventCategory>,
    val title: String,
    val content: String,
    val startDate: String,
    val endDate: String,
    val location: String,
    val posterUrl: String, // временное решение, должна приходить ссылочка на картинку для превью
)
