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

/**
 * Helper function to get available sort fields for specific tab/category combinations
 */
fun getAvailableSortFields(mainTab: MainTab, visitsCategory: VisitsCategory? = null, eventsCategory: EventsCategory? = null): List<SortField> {
    return when (mainTab) {
        MainTab.VISITS -> {
            when (visitsCategory) {
                VisitsCategory.WILLGO -> listOf(SortField.START_DATE) // Мои посещения - по дате
                VisitsCategory.VISITED -> listOf(SortField.START_DATE) // Посещенные - по дате
                null -> listOf(SortField.START_DATE) // fallback
            }
        }
        MainTab.EVENTS -> {
            when (eventsCategory) {
                EventsCategory.DRAFTS -> listOf(SortField.TITLE) // Мои мероприятия - по названию
                EventsCategory.PLANNED -> listOf(SortField.START_DATE) // Запланированные - по дате
                EventsCategory.COMPLETED -> listOf(SortField.START_DATE) // Проведенные - по дате
                null -> listOf(SortField.START_DATE) // fallback
            }
        }
    }
}

/**
 * Helper function to get the default sort field for specific tab/category combinations
 */
fun getDefaultSortField(mainTab: MainTab, visitsCategory: VisitsCategory? = null, eventsCategory: EventsCategory? = null): SortField {
    val availableFields = getAvailableSortFields(mainTab, visitsCategory, eventsCategory)
    return availableFields.firstOrNull() ?: SortField.START_DATE
}