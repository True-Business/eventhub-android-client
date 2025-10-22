package ru.truebusiness.liveposter_android_client.data

import java.util.UUID

data class FilterState(
    // Location filtering
    val location: String? = null,
    
    // Price range filtering
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    
    // Time range filtering
    val minStartTime: String? = null,  // ISO format or "now"
    val maxStartTime: String? = null,  // ISO format or "now"
    
    // Duration filtering (in minutes)
    val minDuration: Int? = null,
    val maxDuration: Int? = null,
    
    // Organizer filtering
    val organizer: String? = null,
    val organizerId: UUID? = null,    // filter by organizer user ID
    
    // Participation filtering
    val userParticipating: Boolean? = null, // null = don't filter
    
    // Event status filtering
    val eventStatus: EventStatus? = null,
    
    // Public/Private filtering
    val isPublic: Boolean? = null, // null = both
    
    // Sorting
    val sortBy: SortField = SortField.START_DATE,
    val sortOrder: SortOrder = SortOrder.ASC,
    
    // Search query
    val query: String = ""
)

enum class SortField {
    START_DATE, TITLE, PRICE, LOCATION
}

enum class SortOrder {
    ASC, DESC
}

// Main tab enum (moved from FilterDialog to have single source of truth)
enum class MainTab { 
    VISITS, 
    EVENTS 
}

// Visits category enum for the visits subcategories
enum class VisitsCategory(val displayName: String) {
    WILLGO("Я пойду"),
    VISITED("Посещенные")
}