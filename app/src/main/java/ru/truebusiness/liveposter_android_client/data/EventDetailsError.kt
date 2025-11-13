package ru.truebusiness.liveposter_android_client.data

sealed class EventDetailsError(message: String) : Throwable(message) {
    object EventNotFound : EventDetailsError("Мероприятие не найдено")
    object EventFinished : EventDetailsError("Мероприятие уже завершено")
    object ParticipantsLimitReached : EventDetailsError("Лимит участников достигнут")
}