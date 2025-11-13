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
    val schedule: String = "",
    val city: String = "",
    val address: String = "",
    val isClosed: Boolean = false,
    val participantsCount: Int = 0,
    val participantLimit: Int? = null,
    val description: String = "",
    val howToGet: String = "",
    val organizer: User? = null,
    val isUserParticipant: Boolean = false,
    val isFinished: Boolean = false,
    val participants: List<User> = emptyList(),
    val posts: List<EventPost> = emptyList(),
    val canManage: Boolean = false,
    val shareLink: String = ""
)
