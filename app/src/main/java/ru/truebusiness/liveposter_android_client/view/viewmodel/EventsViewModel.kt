package ru.truebusiness.liveposter_android_client.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.repository.EventRepository

/**
 * Данный класс отвечает за управлением бизнесс логикой меоприятий. Он хранит и
 * управляет состоянием мероприятий, которые отображаются на экране.
 */
class EventsViewModel: ViewModel() {

    private val repository = EventRepository()
    private val _events = MutableLiveData<List<Event>>(emptyList())
    val events: LiveData<List<Event>> = _events

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadEvents() {
        if (_isLoading.value == true) return

        _isLoading.value = true
        repository.fetchEventsMock { newEvents ->
            _isLoading.value = false
            newEvents?.let {
                _events.value = _events.value.orEmpty() + it
            }
        }
    }
}