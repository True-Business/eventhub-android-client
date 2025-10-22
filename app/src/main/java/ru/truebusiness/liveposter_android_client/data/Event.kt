package ru.truebusiness.liveposter_android_client.data

import java.util.UUID

data class Event(
    val id: UUID,
    val category: List<EventCategory>,
    val title: String,
    val content: String,
    val startDate: String, // ISO format: "2025-05-07T13:00:00"
    val endDate: String,   // ISO format: "2025-05-07T15:00:00"
    val location: String,
    val posterUrl: String, // временное решение, должна приходить ссылочка на картинку для превью
    
    // New filtering fields
    val price: Double?,           // null for free events
    val duration: Int?,          // duration in minutes
    val organizer: String?,      // organizer name or null
    val organizerId: UUID?,      // organizer user ID or null
    val isUserParticipating: Boolean,
    val eventStatus: EventStatus,
    val isPublic: Boolean        // true for public, false for private
)

enum class EventStatus {
    DRAFT,      // черновик
    PLANNED,    // запланировано
    COMPLETED,  // завершено
    CANCELLED   // отменено
}

// Events category enum for events subcategories
enum class EventsCategory(val displayName: String) {
    DRAFTS("Черновики"),
    PLANNED("Запланированные"),
    COMPLETED("Проведенные")
}
