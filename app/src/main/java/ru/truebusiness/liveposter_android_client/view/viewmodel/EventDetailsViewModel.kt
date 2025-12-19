package ru.truebusiness.liveposter_android_client.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.data.EventDetailsError
import ru.truebusiness.liveposter_android_client.data.EventPost
import ru.truebusiness.liveposter_android_client.data.User
import ru.truebusiness.liveposter_android_client.repository.EventDetailsRepository
import ru.truebusiness.liveposter_android_client.utils.DateUtils
import ru.truebusiness.liveposter_android_client.utils.DateUtils.formatEventDate

class EventDetailsViewModel(
    private val repository: EventDetailsRepository = EventDetailsRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventDetailsUiState())
    val uiState: StateFlow<EventDetailsUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<EventDetailsEvent>()
    val events: SharedFlow<EventDetailsEvent> = _events.asSharedFlow()

    private val _event = MutableLiveData<List<Event>>(emptyList())
    val event: LiveData<List<Event>> = _event

    private var currentEventId: String? = null

    fun loadEvent(eventId: String, initialEvent: Event? = null, force: Boolean = false) {
        if (!force && currentEventId == eventId && !_uiState.value.isLoading && _uiState.value.event != null) return
        if (!force && currentEventId == eventId && _uiState.value.isLoading) return

        currentEventId = eventId

        initialEvent?.let {
            repository.cacheEvent(it)
            updateStateWithEvent(it)
        }

        viewModelScope.launch {
            val shouldShowLoader = _uiState.value.event == null
            _uiState.update { it.copy(isLoading = shouldShowLoader, errorMessage = null) }

            repository.getEvent(eventId)
                .onSuccess { event ->
                    repository.cacheEvent(event)
                    updateStateWithEvent(event)
                }
                .onFailure { throwable -> handleError(throwable, clearEvent = _uiState.value.event == null) }
        }
    }

    fun onPrimaryAction() {
        val state = _uiState.value
        val primaryButton = state.actions.primaryButton ?: return
        if (!primaryButton.enabled) return

        viewModelScope.launch {

            val result = when (primaryButton.action) {
                EventPrimaryAction.JOIN -> Result.failure(RuntimeException("Сервис временно недоступен"))
                EventPrimaryAction.LEAVE -> Result.failure(RuntimeException("Сервис временно недоступен"))
                EventPrimaryAction.NONE -> Result.success(state.event)
            }

            result
                .onSuccess { event -> updateStateWithEvent(event) }
                .onFailure { throwable -> handleError(throwable) }

        }
    }

    private fun handleError(throwable: Throwable, clearEvent: Boolean = false) {
        val message = resolveErrorMessage(throwable)
        _uiState.update { state ->
            val updated = state.copy(
                isLoading = false,
                errorMessage = message
            )

            if (clearEvent) {
                updated.copy(
                    event = null,
                    mainInfo = EventMainInfoUiState(),
                    actions = EventActionsUiState(),
                    additionalInfo = EventAdditionalInfoUiState(),
                    posts = emptyList()
                )
            } else {
                updated
            }
        }
        viewModelScope.launch {
            _events.emit(EventDetailsEvent.Error(message))
        }
    }

    private fun resolveErrorMessage(throwable: Throwable): String = when (throwable) {
        is EventDetailsError -> throwable.message ?: "Произошла ошибка"
        else -> throwable.message ?: "Произошла ошибка"
    }

    private fun updateStateWithEvent(event: Event?) {
        event?.let {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = null,
                    event = event,
                    mainInfo = buildMainInfoState(event),
                    actions = buildActionsState(event),
                    additionalInfo = buildAdditionalInfoState(event)
                )
            }
        }
    }

    private fun buildMainInfoState(event: Event): EventMainInfoUiState {
        val startDateText = formatEventDate(event.startDate)
        val endDateText = formatEventDate(event.endDate)
        val scheduleText = if (startDateText == endDateText) startDateText else "$startDateText - $endDateText"

        val participantsLimit = event.peopleLimit
        val participantsText = buildString {
            append(event.participantsCount)
            participantsLimit.let { limit ->
                append("/")
                append(limit)
            }
        }.ifBlank { null }

        return EventMainInfoUiState(
            title = event.title,
            posterUrl = event.posterUrl.takeIf { it.isNotBlank() },
            scheduleText = scheduleText,
            location = EventLocationUiState(
                city = event.city.takeIf { it.isNotBlank() },
                address = event.address.takeIf { it.isNotBlank() },
                location = event.location.takeIf { it.isNotBlank() }
            ),
            status = if (!event.open) {
                EventStatusUiState(
                    label = "Закрытое мероприятие",
                    type = EventStatusType.CLOSED
                )
            } else {
                EventStatusUiState(
                    label = "Открытое мероприятие",
                    type = EventStatusType.OPEN
                )
            },
            participantsText = participantsText
        )
    }

    private fun buildActionsState(event: Event): EventActionsUiState {
        val participantsLimit = event.peopleLimit
        val isFinished = event.isFinished
        val isParticipant = event.isUserParticipating

        val participantsLimitReached = participantsLimit.let { limit ->
            event.participantsCount >= limit
        }

        val primaryButton = when {
            isFinished && isParticipant -> EventPrimaryButtonState(
                text = "Вы участвовали",
                enabled = false,
                action = EventPrimaryAction.NONE
            )

            isFinished && !isParticipant -> null

            isParticipant -> EventPrimaryButtonState(
                text = "Не смогу пойти",
                enabled = true,
                action = EventPrimaryAction.LEAVE
            )

            participantsLimitReached -> EventPrimaryButtonState(
                text = "Лимит участников достигнут",
                enabled = false,
                action = EventPrimaryAction.NONE
            )

            else -> EventPrimaryButtonState(
                text = "Хочу пойти",
                enabled = true,
                action = EventPrimaryAction.JOIN
            )
        }

        return EventActionsUiState(
            primaryButton = primaryButton
        )
    }
    private fun buildAdditionalInfoState(event: Event): EventAdditionalInfoUiState {
        val description = event.description.takeIf { it.isNotBlank() }
            ?: event.content.takeIf { it.isNotBlank() }
        val howToGet = event.howToGet.takeIf { it.isNotBlank() }
            ?: event.route.takeIf { it.isNotBlank() }
        val organizer = event.organizer.takeIf { !it.isNullOrBlank() }

        return EventAdditionalInfoUiState(
            description = description,
            howToGet = howToGet,
            organizer = organizer
        )
    }
}

data class EventDetailsUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val event: Event? = null,
    val mainInfo: EventMainInfoUiState = EventMainInfoUiState(),
    val actions: EventActionsUiState = EventActionsUiState(),
    val additionalInfo: EventAdditionalInfoUiState = EventAdditionalInfoUiState(),
    val posts: List<EventPost> = emptyList()
)

data class EventMainInfoUiState(
    val title: String = "",
    val posterUrl: String? = null,
    val scheduleText: String? = null,
    val location: EventLocationUiState = EventLocationUiState(),
    val status: EventStatusUiState = EventStatusUiState(),
    val participantsText: String? = null
)

data class EventLocationUiState(
    val city: String? = null,
    val address: String? = null,
    val location: String? = null
)

data class EventStatusUiState(
    val label: String = "",
    val type: EventStatusType = EventStatusType.OPEN
)

enum class EventStatusType { OPEN, CLOSED }

data class EventActionsUiState(
    val primaryButton: EventPrimaryButtonState? = null,
    val participants: List<User> = emptyList(),
)

data class EventPrimaryButtonState(
    val text: String,
    val enabled: Boolean,
    val action: EventPrimaryAction
)

enum class EventPrimaryAction { JOIN, LEAVE, NONE }

data class EventAdditionalInfoUiState(
    val description: String? = null,
    val howToGet: String? = null,
    val organizer: String? = null
) {
    val isVisible: Boolean
        get() = !description.isNullOrBlank() || !howToGet.isNullOrBlank() || organizer != null
}

sealed interface EventDetailsEvent {
    data class Error(val message: String) : EventDetailsEvent
}