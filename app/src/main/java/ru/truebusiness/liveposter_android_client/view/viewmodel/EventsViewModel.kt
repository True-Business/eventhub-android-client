package ru.truebusiness.liveposter_android_client.view.viewmodel

import androidx.lifecycle.ViewModel
import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.repository.EventRepository

/**
 * Данный класс отвечает за управлением бизнесс логикой меоприятий. Он хранит и
 * управляет состоянием мероприятий, которые отображаются на экране.
 */
class EventsViewModel: ViewModel() {

    private val repository = EventRepository()
    val events: MutableList<Event>? = null

    fun loadEvents() {

    }
}