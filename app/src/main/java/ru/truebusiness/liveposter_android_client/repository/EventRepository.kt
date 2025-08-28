package ru.truebusiness.liveposter_android_client.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.data.EventCategory
import ru.truebusiness.liveposter_android_client.repository.api.RetrofitInstance
import ru.truebusiness.liveposter_android_client.repository.mocks.mockList

/**
 * Данный класс отвечает за логику загрузки мероприятий с бекенда
 */
class EventRepository {

    /**
     * Данный метод загружает мероприятия указанной категории с бекенда
     *
     * @param onResult лямбда, которая будет обработчиком полученных мероприятий
     * @param category категория мероприятий, которые должны быть загружены
     */
    fun fetchEvents(onResult: (List<Event>?) -> Unit,
                    category: EventCategory? = EventCategory.ALL) {
        RetrofitInstance.eventApi
            .getEvents(category.toString())
            .enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>?>, response: Response<List<Event>?>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(
                call: Call<List<Event>?>,
                t: Throwable
            ) {
                TODO("Not yet implemented")
            }

        })
    }

    /**
     * Мок-метод, который возвращает заготовленные данные для тестирования
     */
    fun fetchEventsMock(category: EventCategory? = EventCategory.ALL,
                        onResult: (List<Event>?) -> Unit) {
        onResult(mockList.filter {
            it.category.contains(category)
        })
    }

    /**
     * Метод загружает одно мероприятие с бекенда по его id
     */
    fun fetchEvent(eventId: String) {
        RetrofitInstance.eventApi.getEvent(eventId)
    }

    /**
     * Метод достаёт мок мероприятия по его id
     */
    fun fetchEventMock(eventId: String): Event? {
        return mockList.filter {
            it.id.toString().equals( eventId)
        }.first()
    }

    /**
     * Метод делает поиск мероприятий по query
     */
    fun searchMockEvents(query: String, onResult: (List<Event>?) -> Unit) {
        onResult(mockList.filter {
            it.title.contains(query, ignoreCase = true)
        })
    }
}