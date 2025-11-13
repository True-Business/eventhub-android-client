package ru.truebusiness.liveposter_android_client.repository

import ru.truebusiness.liveposter_android_client.data.Event


interface EventDetailsRepository {
    suspend fun getEvent(eventId: String): Result<Event>
    suspend fun joinEvent(eventId: String): Result<Event>
    suspend fun leaveEvent(eventId: String): Result<Event>
}