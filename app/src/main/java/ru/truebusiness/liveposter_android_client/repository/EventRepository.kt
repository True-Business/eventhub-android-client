package ru.truebusiness.liveposter_android_client.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.repository.api.RetrofitInstance
import ru.truebusiness.liveposter_android_client.repository.mocks.mockList
import java.util.UUID

/**
 * Данный класс отвечает за логику загрузки мероприятий с бекенда
 */
class EventRepository {

    /**
     * Данный метод загружает мероприятия с бекенда
     *
     * @param onResult лямбда, которая будет обработчиком полученных мероприятий
     */
    fun fetchEvents(onResult: (List<Event>?) -> Unit) {
        RetrofitInstance.eventApi.getEvents().enqueue(object : Callback<List<Event>> {
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
    fun fetchEventsMock(onResult: (List<Event>?) -> Unit) {
        onResult(mockList)
    }
}