package ru.truebusiness.liveposter_android_client.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.data.EventCategory
import ru.truebusiness.liveposter_android_client.data.FilterState
import ru.truebusiness.liveposter_android_client.data.MainTab
import ru.truebusiness.liveposter_android_client.data.VisitsCategory
import ru.truebusiness.liveposter_android_client.data.EventStatus
import ru.truebusiness.liveposter_android_client.data.SortField
import ru.truebusiness.liveposter_android_client.data.SortOrder
import ru.truebusiness.liveposter_android_client.data.EventsCategory
import ru.truebusiness.liveposter_android_client.data.getDefaultSortField
import ru.truebusiness.liveposter_android_client.repository.EventRepository
import ru.truebusiness.liveposter_android_client.repository.mocks.mockCurrentUserId
import ru.truebusiness.liveposter_android_client.utils.DateUtils

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

    // New comprehensive filtering properties
    private val _filterState = MutableLiveData(FilterState())
    val filterState: LiveData<FilterState> = _filterState

    private val _currentMainTab = MutableLiveData(MainTab.VISITS)
    val currentMainTab: LiveData<MainTab> = _currentMainTab

    private val _currentVisitsCategory = MutableLiveData(VisitsCategory.WILLGO)
    val currentVisitsCategory: LiveData<VisitsCategory> = _currentVisitsCategory

    private val _currentEventsCategory = MutableLiveData(EventsCategory.DRAFTS)
    val currentEventsCategory: LiveData<EventsCategory> = _currentEventsCategory

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

    // New comprehensive filtering methods

    /**
     * Set main tab and apply appropriate filters
     */
    fun setMainTab(tab: MainTab) {
        _currentMainTab.value = tab
        applyTabSpecificFilters()
    }

    /**
     * Set visits subcategory and apply filters
     */
    fun setVisitsCategory(category: VisitsCategory) {
        _currentVisitsCategory.value = category
        applyTabSpecificFilters()
    }

    /**
     * Set events subcategory and apply filters
     */
    fun setEventsCategory(category: EventsCategory) {
        _currentEventsCategory.value = category
        applyTabSpecificFilters()
    }

    /**
     * Apply tab-specific filter presets
     */
    private fun applyTabSpecificFilters() {
        val tab = _currentMainTab.value ?: MainTab.VISITS

        if (tab == MainTab.VISITS) {
            val category = _currentVisitsCategory.value ?: VisitsCategory.WILLGO
            when (category) {
                VisitsCategory.WILLGO -> applyWillGoFilters()
                VisitsCategory.VISITED -> applyVisitedFilters()
            }
        } else if (tab == MainTab.EVENTS) {
            val category = _currentEventsCategory.value ?: EventsCategory.DRAFTS
            when (category) {
                EventsCategory.DRAFTS -> applyDraftsFilters()
                EventsCategory.PLANNED -> applyPlannedFilters()
                EventsCategory.COMPLETED -> applyCompletedFilters()
            }
        }

        loadEventsWithFilters()
        sortEventsLocally()
    }

    /**
     * Apply "Я пойду" filters
     */
    private fun applyWillGoFilters() {
        val now = DateUtils.getCurrentDateTimeString()
        _filterState.value = FilterState(
            location = null, // все
            minPrice = null,
            maxPrice = null,
            minStartTime = now, // сейчас
            maxStartTime = null,
            minDuration = null,
            maxDuration = null,
            organizer = null, // не учитывать
            userParticipating = true, // участвую
            eventStatus = EventStatus.PUBLISHED, // запланировано
            isPublic = null, // все
            sortBy = getDefaultSortField(MainTab.VISITS, VisitsCategory.WILLGO),
            sortOrder = SortOrder.ASC, // по возрастанию
            query = ""
        )
    }

    /**
     * Apply "Посещенные" filters
     */
    private fun applyVisitedFilters() {
        val now = DateUtils.getCurrentDateTimeString()
        _filterState.value = FilterState(
            location = null, // все
            minPrice = null,
            maxPrice = null,
            minStartTime = null,
            maxStartTime = now, // сейчас
            minDuration = null,
            maxDuration = null,
            organizer = null, // не учитывать
            userParticipating = true, // участвую
            eventStatus = EventStatus.COMPLETED, // завершено
            isPublic = null, // все
            sortBy = getDefaultSortField(MainTab.VISITS, VisitsCategory.VISITED),
            sortOrder = SortOrder.DESC, // по убыванию
            query = ""
        )
    }

    /**
     * Apply "Черновики" filters
     */
    private fun applyDraftsFilters() {
        _filterState.value = FilterState(
            location = null, // все
            minPrice = null,
            maxPrice = null,
            minStartTime = null,
            maxStartTime = null,
            minDuration = null,
            maxDuration = null,
            organizer = null, // не учитывать
            organizerId = mockCurrentUserId, // id-пользователя
            userParticipating = null, // все
            eventStatus = EventStatus.DRAFT, // черновик
            isPublic = null, // все
            sortBy = getDefaultSortField(MainTab.EVENTS, eventsCategory = EventsCategory.DRAFTS),
            sortOrder = SortOrder.ASC, // по возрастанию (а -> я)
            query = ""
        )
    }

    /**
     * Apply "Запланированные" filters
     */
    private fun applyPlannedFilters() {
        val now = DateUtils.getCurrentDateTimeString()
        _filterState.value = FilterState(
            location = null, // все
            minPrice = null,
            maxPrice = null,
            minStartTime = now, // сейчас
            maxStartTime = null,
            minDuration = null,
            maxDuration = null,
            organizer = null, // не учитывать
            organizerId = mockCurrentUserId, // id-пользователя
            userParticipating = null, // все
            eventStatus = EventStatus.PUBLISHED, // запланировано
            isPublic = null, // все
            sortBy = getDefaultSortField(MainTab.EVENTS, eventsCategory = EventsCategory.PLANNED),
            sortOrder = SortOrder.ASC, // по возрастанию (сначала ближайшие)
            query = ""
        )
    }

    /**
     * Apply "Проведенные" filters
     */
    private fun applyCompletedFilters() {
        val now = DateUtils.getCurrentDateTimeString()
        _filterState.value = FilterState(
            location = null, // все
            minPrice = null,
            maxPrice = null,
            minStartTime = null,
            maxStartTime = now, // сейчас
            minDuration = null,
            maxDuration = null,
            organizer = null, // не учитывать
            organizerId = mockCurrentUserId, // id-пользователя
            userParticipating = null, // все
            eventStatus = EventStatus.COMPLETED, // завершено
            isPublic = null, // все
            sortBy = getDefaultSortField(MainTab.EVENTS, eventsCategory = EventsCategory.COMPLETED),
            sortOrder = SortOrder.DESC, // по убыванию (сначала ближайшие)
            query = ""
        )
    }

    /**
     * Update sort order from filter dialog - applies sorting locally to existing data
     */
    fun updateSortOrder(sortBy: SortField, sortOrder: SortOrder) {
        val currentFilter = _filterState.value ?: FilterState()
        _filterState.value = currentFilter.copy(
            sortBy = sortBy,
            sortOrder = sortOrder
        )
        // Apply sorting locally to existing events without API call
        sortEventsLocally()
    }

    /**
     * Sort events locally based on current filter state
     */
    private fun sortEventsLocally() {
        val currentEvents = _events.value ?: return
        val filter = _filterState.value ?: return
        val sortedEvents = when (filter.sortBy) {
            SortField.START_DATE -> {
                if (filter.sortOrder == SortOrder.ASC) {
                    currentEvents.sortedBy { it.startDate }
                } else {
                    currentEvents.sortedByDescending { it.startDate }
                }
            }

            SortField.TITLE -> {
                if (filter.sortOrder == SortOrder.ASC) {
                    currentEvents.sortedBy { it.title }
                } else {
                    currentEvents.sortedByDescending { it.title }
                }
            }

            SortField.PRICE -> {
                if (filter.sortOrder == SortOrder.ASC) {
                    currentEvents.sortedBy { it.price ?: Double.MAX_VALUE }
                } else {
                    currentEvents.sortedByDescending { it.price ?: 0.0 }
                }
            }

            SortField.LOCATION -> {
                if (filter.sortOrder == SortOrder.ASC) {
                    currentEvents.sortedBy { it.location }
                } else {
                    currentEvents.sortedByDescending { it.location }
                }
            }
        }

        _events.value = sortedEvents
    }

    /**
     * Load events with current filters
     */
    fun loadEventsWithFilters() {
        if (_isLoading.value == true) return

        _isLoading.value = true
        val filter = _filterState.value ?: FilterState()

        repository.fetchEventsWithFilterMock(filter) { newEvents ->
            _isLoading.value = false
            newEvents?.let {
                _events.value = it
            }
        }
    }

    /**
     * Initialize the ViewModel with default state
     */
    fun initialize() {
        applyTabSpecificFilters()
    }
}