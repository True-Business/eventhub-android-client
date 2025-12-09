package ru.truebusiness.liveposter_android_client.data

import java.util.UUID

data class EventDetails(
    val event: Event,
    val city: String,
    val address: String,
    val isPrivate: Boolean,
    val isCompleted: Boolean,
    val participantsCount: Int,
    val participantsLimit: Int?,
    val schedule: String,
    val description: String,
    val howToGet: String,
    val organizer: EventOrganizer,
    val participants: List<User>,
    val posts: List<EventPost>,
    val isParticipant: Boolean,
    val wasParticipant: Boolean,
    val canManage: Boolean,
)

data class EventOrganizer(
    val id: UUID,
    val name: String,
    val avatarUrl: String,
)
