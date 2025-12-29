package ru.truebusiness.liveposter_android_client.data.dto

import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.data.EventCategory
import ru.truebusiness.liveposter_android_client.data.EventStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

/**
 * Extension functions for converting between API DTOs and domain Event model
 */

/**
 * Converts EventDto from API response to domain Event model
 */
fun EventDto.toDomainEvent(): Event {
    val formatter = DateTimeFormatter.ISO_INSTANT
    val startDateTime = parseIsoDateTime(this.startDateTime)
    val endDateTime = parseIsoDateTime(this.endDateTime)

    return Event(
        id = UUID.fromString(this.id),
        category = parseCategoryString(this.category),
        title = this.name,
        content = this.description,
        startDate = startDateTime,
        endDate = endDateTime,
        location = buildLocationString(this.address, this.route, this.city),
        posterUrl = "", // TODO: Implement when API provides poster URL

        // API fields
        organizerId = UUID.fromString(this.organizerId),
        organizationId = UUID.fromString(this.organizationId),
        updatedAt = parseIsoDateTime(this.updatedAt),
        address = this.address,
        route = this.route,
        city = this.city,
        peopleLimit = this.peopleLimit,
        registerEndDateTime = parseIsoDateTime(this.registerEndDateTime),
        withRegister = this.withRegister,
        open = this.open,

        // Existing fields with defaults
        price = if (this.price == 0.0) null else this.price,
        duration = null, // TODO: Calculate from start/end dates if needed
        organizer = null, // TODO: Fetch from user API if needed
        isUserParticipating = this.userParticipant ?: false,
        eventStatus = parseEventStatus(this.status),
        isPublic = this.open,

        participantsCount = this.participantsCount ?: 0,
        isFinished = endDateTime.isBefore(LocalDateTime.now())
    )
}

/**
 * Converts domain Event model to EventCreateUpdateDto for API requests
 */
fun Event.toCreateUpdateDto(): EventCreateUpdateDto {
    return EventCreateUpdateDto(
        name = this.title,
        startDateTime = this.startDate.toString(),
        endDateTime = this.endDate.toString(),
        organizerId = this.organizerId?.toString(),
        organizationId = this.organizationId?.toString(),
        category = this.category.firstOrNull()?.toString() ?: "PLACEHOLDER",
        address = this.address,
        route = this.route,
        description = this.content,
        price = this.price ?: 0.0,
        status = this.eventStatus.name,
        city = this.city,
        peopleLimit = this.peopleLimit,
        registerEndDateTime = this.registerEndDateTime.toString(),
        isWithRegister = this.withRegister,
        isOpen = this.isPublic
    )
}

/**
 * Parses category string from API to List<EventCategory>
 * API returns single category string, we convert to list for UI compatibility
 */
private fun parseCategoryString(categoryString: String): List<EventCategory> {
    return try {
        val category = when (categoryString.uppercase()) {
            "MUSIC" -> EventCategory.MUSIC
            "FILMS" -> EventCategory.FILMS
            "RESTAURANTS" -> EventCategory.RESTAURANTS
            "FESTIVALS" -> EventCategory.FESTIVALS
            "MEETINGS" -> EventCategory.MEETINGS
            "SHOWS" -> EventCategory.SHOWS
            else -> EventCategory.ALL
        }
        listOf(category)
    } catch (e: Exception) {
        listOf(EventCategory.ALL)
    }
}

/**
 * Parses event status string from API to EventStatus enum
 */
private fun parseEventStatus(statusString: String): EventStatus {
    return try {
        when (statusString.uppercase()) {
            "DRAFT" -> EventStatus.DRAFT
            "PUBLISHED" -> EventStatus.PUBLISHED
            "CANCELLED" -> EventStatus.CANCELLED
            "COMPLETED" -> EventStatus.COMPLETED
            else -> EventStatus.DRAFT
        }
    } catch (e: Exception) {
        EventStatus.DRAFT
    }
}

/**
 * Builds location string from address, route, and city components
 */
private fun buildLocationString(address: String, route: String, city: String): String {
    val components = listOfNotNull(
        address.takeIf { it.isNotBlank() },
        route.takeIf { it.isNotBlank() },
        city.takeIf { it.isNotBlank() }
    )
    return components.joinToString(", ")
}

/**
 * Converts a list of EventDto to domain Event list
 */
fun List<EventDto>.toDomainEvents(): List<Event> {
    return this.map { it.toDomainEvent() }
}

/**
 * Parses ISO 8601 datetime strings (both with and without 'Z' suffix)
 */
fun parseIsoDateTime(dateTimeString: String?): LocalDateTime {
    if (dateTimeString == null) {
        return LocalDateTime.now()
    }
    return try {
        // Try parsing with 'Z' suffix first (UTC)
        if (dateTimeString.endsWith("Z")) {
            val instant = java.time.Instant.parse(dateTimeString)
            LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault())
        } else {
            // Try parsing without timezone
            LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME)
        }
    } catch (e: Exception) {
        // Fallback: try parsing without milliseconds
        try {
            val cleanedString = dateTimeString.replace(Regex("\\.\\d+Z"), "Z")
            if (cleanedString.endsWith("Z")) {
                val instant = java.time.Instant.parse(cleanedString)
                LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault())
            } else {
                LocalDateTime.parse(cleanedString, DateTimeFormatter.ISO_DATE_TIME)
            }
        } catch (e2: Exception) {
            // Last resort: return current time
            LocalDateTime.now()
        }
    }
}