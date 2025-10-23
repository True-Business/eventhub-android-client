package ru.truebusiness.liveposter_android_client.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtils {
    private val outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm", Locale("ru"))

    fun formatEventDate(dateTime: LocalDateTime): String {
        return try {
            dateTime.format(outputFormatter)
        } catch (e: Exception) {
            dateTime.toString() // Return original if formatting fails
        }
    }
    
    fun formatEventDate(dateString: String): String {
        return try {
            val dateTime = LocalDateTime.parse(dateString)
            dateTime.format(outputFormatter)
        } catch (e: Exception) {
            dateString // Return original if parsing fails
        }
    }
    
    fun isEventInFuture(dateTime: LocalDateTime): Boolean {
        return dateTime.isAfter(LocalDateTime.now())
    }
    
    fun isEventInFuture(dateString: String): Boolean {
        return try {
            val eventDate = LocalDateTime.parse(dateString)
            eventDate.isAfter(LocalDateTime.now())
        } catch (e: Exception) {
            false
        }
    }
    
    fun isEventInPast(dateTime: LocalDateTime): Boolean {
        return dateTime.isBefore(LocalDateTime.now())
    }
    
    fun isEventInPast(dateString: String): Boolean {
        return try {
            val eventDate = LocalDateTime.parse(dateString)
            eventDate.isBefore(LocalDateTime.now())
        } catch (e: Exception) {
            false
        }
    }
    
    fun getCurrentDateTimeString(): String {
        return LocalDateTime.now().toString()
    }
    
    fun getCurrentDateTime(): LocalDateTime {
        return LocalDateTime.now()
    }
}