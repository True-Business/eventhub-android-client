package ru.truebusiness.liveposter_android_client.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.data.EventCategory
import ru.truebusiness.liveposter_android_client.data.FilterState
import ru.truebusiness.liveposter_android_client.data.SortField
import ru.truebusiness.liveposter_android_client.data.SortOrder
import ru.truebusiness.liveposter_android_client.data.dto.EventDto
import ru.truebusiness.liveposter_android_client.data.dto.toDomainEvents
import ru.truebusiness.liveposter_android_client.data.dto.toCreateUpdateDto
import ru.truebusiness.liveposter_android_client.data.dto.toDomainEvent
import ru.truebusiness.liveposter_android_client.repository.api.RetrofitInstance
import ru.truebusiness.liveposter_android_client.repository.mocks.mockEventList
import java.time.LocalDateTime

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
            .enqueue(object : Callback<List<EventDto>> {
            override fun onResponse(call: Call<List<EventDto>>, response: Response<List<EventDto>>) {
                if (response.isSuccessful) {
                    val events = response.body()?.toDomainEvents()
                    onResult(events)
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<List<EventDto>>, t: Throwable) {
                onResult(null)
            }
        })
    }

    /**
     * Мок-метод, который возвращает заготовленные данные для тестирования
     */
    fun fetchEventsMock(category: EventCategory? = EventCategory.ALL,
                        onResult: (List<Event>?) -> Unit) {
        onResult(mockEventList.filter {
            it.category.contains(category)
        })
    }

    /**
     * Метод загружает одно мероприятие с бекенда по его id
     */
    fun fetchEvent(eventId: String, onResult: (Event?) -> Unit) {
        RetrofitInstance.eventApi.getEvent(eventId)
            .enqueue(object : Callback<EventDto> {
                override fun onResponse(call: Call<EventDto>, response: Response<EventDto>) {
                    if (response.isSuccessful) {
                        val event = response.body()?.toDomainEvent()
                        onResult(event)
                    } else {
                        onResult(null)
                    }
                }

                override fun onFailure(call: Call<EventDto>, t: Throwable) {
                    onResult(null)
                }
            })
    }

    /**
     * Метод достаёт мок мероприятия по его id
     */
    fun fetchEventMock(eventId: String): Event? {
        return mockEventList.filter {
            it.id.toString().equals( eventId)
        }.first()
    }

    /**
     * Метод делает поиск мероприятий по query
     */
    fun searchMockEvents(query: String, onResult: (List<Event>?) -> Unit) {
        onResult(mockEventList.filter {
            it.title.contains(query, ignoreCase = true)
        })
    }

    /**
     * Метод фильтрует мероприятия по комплексным критериям
     */
    fun fetchEventsWithFilterMock(
        filter: FilterState,
        onResult: (List<Event>?) -> Unit
    ) {
        var filteredEvents = mockEventList

        // Apply location filter
        filter.location?.let { location ->
            filteredEvents = filteredEvents.filter {
                it.location.contains(location, ignoreCase = true)
            }
        }

        // Apply price range filter
        filter.minPrice?.let { minPrice ->
            filteredEvents = filteredEvents.filter {
                (it.price ?: 0.0) >= minPrice
            }
        }
        filter.maxPrice?.let { maxPrice ->
            filteredEvents = filteredEvents.filter {
                (it.price ?: Double.MAX_VALUE) <= maxPrice
            }
        }

        // Apply time range filter
        val now = LocalDateTime.now()
        filter.minStartTime?.let { minTime ->
            val minDateTime = if (minTime == "now") now
            else LocalDateTime.parse(minTime)
            filteredEvents = filteredEvents.filter {
                it.startDate >= minDateTime
            }
        }
        filter.maxStartTime?.let { maxTime ->
            val maxDateTime = if (maxTime == "now") now
            else LocalDateTime.parse(maxTime)
            filteredEvents = filteredEvents.filter {
                it.startDate <= maxDateTime
            }
        }

        // Apply duration filter
        filter.minDuration?.let { minDuration ->
            filteredEvents = filteredEvents.filter {
                (it.duration ?: Int.MAX_VALUE) >= minDuration
            }
        }
        filter.maxDuration?.let { maxDuration ->
            filteredEvents = filteredEvents.filter {
                (it.duration ?: 0) <= maxDuration
            }
        }

        // Apply organizer filter
        filter.organizer?.let { organizer ->
            filteredEvents = filteredEvents.filter {
                it.organizer?.contains(organizer, ignoreCase = true) == true
            }
        }

        // Apply organizer ID filter
        filter.organizerId?.let { organizerId ->
            filteredEvents = filteredEvents.filter {
                it.organizerId == organizerId
            }
        }

        // Apply participation filter
        filter.userParticipating?.let { participating ->
            filteredEvents = filteredEvents.filter {
                it.isUserParticipating == participating
            }
        }

        // Apply event status filter
        filter.eventStatus?.let { status ->
            filteredEvents = filteredEvents.filter {
                it.eventStatus == status
            }
        }

        // Apply public/private filter
        filter.isPublic?.let { isPublic ->
            filteredEvents = filteredEvents.filter {
                it.isPublic == isPublic
            }
        }

        // Apply search query
        if (filter.query.isNotBlank()) {
            filteredEvents = filteredEvents.filter {
                it.title.contains(filter.query, ignoreCase = true) ||
                it.content.contains(filter.query, ignoreCase = true) ||
                it.location.contains(filter.query, ignoreCase = true)
            }
        }

        // Apply sorting
        filteredEvents = when (filter.sortBy) {
            SortField.START_DATE -> {
                if (filter.sortOrder == SortOrder.ASC) {
                    filteredEvents.sortedBy { it.startDate }
                } else {
                    filteredEvents.sortedByDescending { it.startDate }
                }
            }
            SortField.TITLE -> {
                if (filter.sortOrder == SortOrder.ASC) {
                    filteredEvents.sortedBy { it.title }
                } else {
                    filteredEvents.sortedByDescending { it.title }
                }
            }
            SortField.PRICE -> {
                if (filter.sortOrder == SortOrder.ASC) {
                    filteredEvents.sortedBy { it.price ?: Double.MAX_VALUE }
                } else {
                    filteredEvents.sortedByDescending { it.price ?: 0.0 }
                }
            }
            SortField.LOCATION -> {
                if (filter.sortOrder == SortOrder.ASC) {
                    filteredEvents.sortedBy { it.location }
                } else {
                    filteredEvents.sortedByDescending { it.location }
                }
            }
        }

        // TODO убрать после добавления взаимодействия с бэком
        filteredEvents = mockEventList

        onResult(filteredEvents)
    }

    /**
     * Создает новое событие через API
     */
    fun createEvent(event: Event, onResult: (Event?) -> Unit) {
        RetrofitInstance.eventApi.createEvent(event.toCreateUpdateDto())
            .enqueue(object : Callback<EventDto> {
                override fun onResponse(call: Call<EventDto>, response: Response<EventDto>) {
                    if (response.isSuccessful) {
                        val createdEvent = response.body()?.toDomainEvent()
                        onResult(createdEvent)
                    } else {
                        onResult(null)
                    }
                }

                override fun onFailure(call: Call<EventDto>, t: Throwable) {
                    onResult(null)
                }
            })
    }

    /**
     * Обновляет существующее событие через API
     */
    fun updateEvent(eventId: String, event: Event, onResult: (Event?) -> Unit) {
        RetrofitInstance.eventApi.updateEvent(eventId, event.toCreateUpdateDto())
            .enqueue(object : Callback<EventDto> {
                override fun onResponse(call: Call<EventDto>, response: Response<EventDto>) {
                    if (response.isSuccessful) {
                        val updatedEvent = response.body()?.toDomainEvent()
                        onResult(updatedEvent)
                    } else {
                        onResult(null)
                    }
                }

                override fun onFailure(call: Call<EventDto>, t: Throwable) {
                    onResult(null)
                }
            })
    }

    /**
     * Удаляет событие в статусе DRAFT через API
     */
    fun deleteEvent(eventId: String, onResult: (Boolean) -> Unit) {
        RetrofitInstance.eventApi.deleteEvent(eventId)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    onResult(response.isSuccessful)
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    onResult(false)
                }
            })
    }
}