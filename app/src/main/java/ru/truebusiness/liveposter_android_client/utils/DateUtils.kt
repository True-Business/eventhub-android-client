package ru.truebusiness.liveposter_android_client.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtils {
    private val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
    private val outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm", Locale("ru"))

    fun formatEventDate(dateString: String): String {
        return try {
            val dateTime = LocalDateTime.parse(dateString, inputFormatter)
            dateTime.format(outputFormatter)
        } catch (e: Exception) {
            dateString // Return original if parsing fails
        }
    }
    
    fun isEventInFuture(dateString: String): Boolean {
        return try {
            val eventDate = LocalDateTime.parse(dateString, inputFormatter)
            eventDate.isAfter(LocalDateTime.now())
        } catch (e: Exception) {
            false
        }
    }
    
    fun isEventInPast(dateString: String): Boolean {
        return try {
            val eventDate = LocalDateTime.parse(dateString, inputFormatter)
            eventDate.isBefore(LocalDateTime.now())
        } catch (e: Exception) {
            false
        }
    }
    
    fun getCurrentDateTimeString(): String {
        return LocalDateTime.now().toString()
    }
}