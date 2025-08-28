package ru.truebusiness.liveposter_android_client.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.data.EventCategory
import ru.truebusiness.liveposter_android_client.repository.EventRepository

/**
 * Данный класс отвечает за управлением бизнесс логикой меоприятий. Он хранит и
 * управляет состоянием мероприятий, которые отображаются на экране.
 */
class EventsViewModel: ViewModel() {

    private val TAG = "EVENT_VIEW_MODEL"

    private val repository = EventRepository()

    private val _events = MutableLiveData<List<Event>>(emptyList())
    val events: LiveData<List<Event>> = _events

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _selectedCategory = MutableLiveData<EventCategory>(EventCategory.ALL)
    val selectedCategory: LiveData<EventCategory> = _selectedCategory

    fun loadEvents() {
        if (_isLoading.value == true) return

        _isLoading.value = true
        Log.d(TAG, "Загрузка новых мероприятий категории $selectedCategory...")

        repository.fetchEventsMock(_selectedCategory.value) { newEvents ->
            _isLoading.value = false
            newEvents?.let {
                _events.value = _events.value.orEmpty() + it
            }
        }

        Log.d(TAG, "Новые мероприятия загружены!")
    }

    /**
     * Метод устанавливает новую категорию событий и загружает их с бекенда
     */
    fun setCategory(category: EventCategory?) {
        Log.d(TAG, "Смена категории на ${category.toString()}")
        _selectedCategory.value = category ?: EventCategory.ALL
        //Очищаем список событий, чтобы на место них добавлялись события определённой категории
        _events.value = emptyList()
        loadEvents()
    }

    /**
     * 🔎 Поиск мероприятий по тексту (идёт на бэкенд)
     */
    fun searchEvents(query: String) {
        if (_isLoading.value == true) return

        _isLoading.value = true
        Log.d(TAG, "Поиск мероприятий по запросу: $query")

        // TODO(e.vartazaryan): Перейти на вызов api, а не использовать моки
        repository.searchMockEvents(query) { newEvents ->
            _isLoading.value = false
            newEvents?.let {
                _events.value = it
            }
        }
    }
}