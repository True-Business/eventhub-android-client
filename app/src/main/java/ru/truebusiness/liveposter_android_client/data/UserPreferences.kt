package ru.truebusiness.liveposter_android_client.data

// Модель для хранения предпочтений
data class UserPreferences(
    val categories: List<String>,
    val openness: String,
    val paymentType: String,
    val priceRange: String,
    val dayTime: String,
    val city: String,
    val ratingImportance: String
)