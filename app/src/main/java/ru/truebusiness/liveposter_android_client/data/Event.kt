package ru.truebusiness.liveposter_android_client.data

import java.time.LocalDateTime
import java.util.UUID

data class Event(
    val id: UUID,
    val category: List<EventCategory>,
    val title: String,
    val content: String,
    val startDate: LocalDateTime, // Event start date and time
    val endDate: LocalDateTime,   // Event end date and time
    val location: String,
    val posterUrl: String, // временное решение, должна приходить ссылочка на картинку для превью

    // API fields
    val organizerId: UUID?,      // organizer user ID from API
    val organizationId: UUID?,   // organization ID from API
    val updatedAt: LocalDateTime, // last update timestamp from API
    val address: String,         // street address from API
    val route: String,          // route/direction from API
    val city: String,           // city from API
    val peopleLimit: Int,       // maximum participants from API
    val registerEndDateTime: LocalDateTime, // registration deadline from API
    val withRegister: Boolean,  // requires registration from API
    val open: Boolean,          // public/private flag from API

    // Existing filtering fields
    val price: Double?,           // null for free events
    val duration: Int?,          // duration in minutes
    val organizer: String?,      // organizer name or null
    val isUserParticipating: Boolean,
    val eventStatus: EventStatus,
    val isPublic: Boolean,        // true for public, false for private

    val schedule: String = "",
    val isClosed: Boolean = false,
    val participantsCount: Int = 0,
    val participantLimit: Int? = null,
    val description: String = "",
    val howToGet: String = "",
    val isUserParticipant: Boolean = false,
    val isFinished: Boolean = false,
    val participants: List<User> = emptyList(),
    val posts: List<EventPost> = emptyList(),
    val canManage: Boolean = false,
    val shareLink: String = ""
)

enum class EventStatus {
    DRAFT,          // черновик
    PUBLISHED,      // опубликовано
    CANCELLED,      // отменено
    COMPLETED       // завершено
}

// Events category enum for events subcategories
enum class EventsCategory(val displayName: String) {
    DRAFTS("Черновики"),
    PLANNED("Запланированные"),
    COMPLETED("Проведенные")
}
