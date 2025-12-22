package ru.truebusiness.liveposter_android_client.repository

import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.data.EventDetailsError
import ru.truebusiness.liveposter_android_client.data.dto.toDomainEvent
import ru.truebusiness.liveposter_android_client.repository.api.EventApi
import ru.truebusiness.liveposter_android_client.repository.api.RetrofitInstance


class EventDetailsRepository(
    private val eventApi: EventApi = RetrofitInstance.eventApi,
) {
    private val cachedEvents = mutableMapOf<String, Event>()

    suspend fun getEvent(eventId: String): Result<Event> {
        cachedEvents[eventId]?.let { event ->
            return Result.success(event)
        }

        val response = eventApi.getEvent(eventId)
        if (response.isSuccessful) {
            val event = response.body()?.toDomainEvent()
                ?: return Result.failure(EventDetailsError.EventNotFound)
            cachedEvents[eventId] = event
            return Result.success(event)
        }

        return Result.failure(EventDetailsError.EventNotFound)
    }

    fun cacheEvent(event: Event) {
        cachedEvents[event.id.toString()] = event
    }

    suspend fun registerForEvent(eventId: String, userId: String): Result<Event> {
        val currentEvent = cachedEvents[eventId] ?: getEvent(eventId).getOrNull()
        currentEvent ?: return Result.failure(EventDetailsError.EventNotFound)

        val response = eventApi.registerForEvent(eventId, userId)

        if (response.isSuccessful) {
            val updatedEvent = currentEvent.copy(
                isUserParticipating = true,
                participantsCount = currentEvent.participantsCount + 1
            )
            cacheEvent(updatedEvent)
            return Result.success(updatedEvent)
        }

        return Result.failure(RuntimeException(response.errorBody()?.string() ?: "Не удалось зарегистрироваться"))
    }

    suspend fun unregisterFromEvent(eventId: String): Result<Event> {
        val currentEvent = cachedEvents[eventId] ?: getEvent(eventId).getOrNull()
        currentEvent ?: return Result.failure(EventDetailsError.EventNotFound)

        val response = eventApi.unregisterFromEvent(eventId)

        if (response.isSuccessful) {
            val updatedEvent = currentEvent.copy(
                isUserParticipating = false,
                participantsCount = (currentEvent.participantsCount - 1).coerceAtLeast(0)
            )
            cacheEvent(updatedEvent)
            return Result.success(updatedEvent)
        }

        return Result.failure(RuntimeException(response.errorBody()?.string() ?: "Не удалось отменить регистрацию"))
    }
}