package ru.truebusiness.liveposter_android_client.data.dto

import ru.truebusiness.liveposter_android_client.data.EventsCategory
import java.time.Instant
import java.util.UUID

/**
 * DTO class that matches the exact API response format from the backend
 * Used for serialization/deserialization of API requests and responses
 */
data class EventDto(
    val id: String,
    val name: String,
    val startDateTime: String,
    val endDateTime: String,
    val updatedAt: String,
    val organizerId: String,
    val organizationId: String,
    val category: String,
    val address: String,
    val route: String,
    val description: String,
    val price: Double,
    val status: String,
    val city: String,
    val peopleLimit: Int,
    val registerEndDateTime: String,
    val withRegister: Boolean,
    val open: Boolean,

    // Дополнительные поля для экрана деталей
    val participantsCount: Int? = null,
    val userParticipant: Boolean? = null,
    val participants: List<EventParticipantDto>? = null
)

/**
 * DTO for creating/updating events via POST/PUT requests
 * Only includes fields that can be modified
 */
data class EventCreateUpdateDto(
    val name: String? = null,
    val startDateTime: String? = null,
    val endDateTime: String? = null,
    val organizerId: String? = null,
    val organizationId: String? = null,
    val category: String? = null,
    val address: String? = null,
    val route: String? = null,
    val description: String? = null,
    val price: Double? = null,
    val status: String? = null,
    val city: String? = null,
    val peopleLimit: Int? = null,
    val registerEndDateTime: String? = null,
    val isWithRegister: Boolean? = null,
    val isOpen: Boolean? = null
)

data class EventParticipantDto(
    val name: String? = null,
    val avatarUrl: String? = null
)

/**
 * DTO для фильтрации событий на сервере при поиске
 */
data class EventSearchFilterDto(
    val city: String? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val startDateTime: Instant? = null,
    val minDurationMinutes: Int? = null,
    val maxDurationMinutes: Int? = null,
    val organizerId: UUID? = null,
    val isParticipant: Boolean? = null,
    val category: EventsCategory? = null,
    val isOpen: Boolean? = null,
)

data class EventRegistrationDto(
    val id: String?,
    val userId: String?,
    val eventId: String?,
    val registeredAt: String?
)