package ru.truebusiness.liveposter_android_client.repository

import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.data.EventDetailsError
import ru.truebusiness.liveposter_android_client.repository.mocks.mockEventList

class MockEventDetailsRepository : EventDetailsRepository {

    private val events = mockEventList.associateBy { it.id.toString() }.toMutableMap()

    override suspend fun getEvent(eventId: String): Result<Event> {
        val event = events[eventId]
            ?: return Result.failure(EventDetailsError.EventNotFound)
        return Result.success(event)
    }

    override suspend fun joinEvent(eventId: String): Result<Event> {
        val event = events[eventId]
            ?: return Result.failure(EventDetailsError.EventNotFound)

        if (event.isFinished) {
            return Result.failure(EventDetailsError.EventFinished)
        }
        if (event.isUserParticipant) {
            return Result.success(event)
        }

        event.participantLimit?.let { limit ->
            if (event.participantsCount >= limit) {
                return Result.failure(EventDetailsError.ParticipantsLimitReached)
            }
        }

        val updated = event.copy(
            isUserParticipant = true,
            participantsCount = event.participantsCount + 1
        )
        events[eventId] = updated
        return Result.success(updated)
    }

    override suspend fun leaveEvent(eventId: String): Result<Event> {
        val event = events[eventId]
            ?: return Result.failure(EventDetailsError.EventNotFound)

        if (event.isFinished) {
            return Result.failure(EventDetailsError.EventFinished)
        }
        if (!event.isUserParticipant) {
            return Result.success(event)
        }

        val updated = event.copy(
            isUserParticipant = false,
            participantsCount = (event.participantsCount - 1).coerceAtLeast(0)
        )
        events[eventId] = updated
        return Result.success(updated)
    }
}