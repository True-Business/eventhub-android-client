package ru.truebusiness.liveposter_android_client.view.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.data.EventCategory
import ru.truebusiness.liveposter_android_client.data.EventStatus
import ru.truebusiness.liveposter_android_client.repository.AuthRepository
import ru.truebusiness.liveposter_android_client.repository.EventRepository
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

class EventCreationViewModel(authRepository: AuthRepository) : ViewModel() {

    private val eventRepository = EventRepository()
    var isFirstPage by mutableStateOf(true)
    var infoState by mutableStateOf(EventInfoState())
        private set
    var settingsState by mutableStateOf(EventSettingsState())
        private set

    init {
        authRepository.currentUser
            .onEach { user ->
                if (user != null) {
                    infoState = infoState.copy(organizerId = user.id)
                }
            }
            .launchIn(viewModelScope)
    }

    val isInfoValid: Boolean
        get() = infoState.run {
            organizerId != null &&
                    title.length in 3..128 &&
                    description.length in 3..1024 &&
                    city.isNotBlank() &&
                    address.isNotBlank() &&
                    howToGet.isNotBlank() &&
                    startDate >= LocalDate.now() &&
                    endDate >= LocalDate.now() &&
                    LocalDateTime.of(
                        infoState.endDate,
                        infoState.endTime
                    ) > LocalDateTime.of(infoState.startDate, infoState.startTime)
        }

    fun onNextPage() {
        isFirstPage = false
    }

    fun onPrevPage() {
        isFirstPage = true
    }

    fun updateTitle(v: String) {
        infoState = infoState.copy(title = v)
    }

    fun updateDescription(v: String) {
        infoState = infoState.copy(description = v)
    }

    fun updateCity(v: String) {
        infoState = infoState.copy(city = v)
    }

    fun updateAddress(v: String) {
        infoState = infoState.copy(address = v)
    }

    fun updateHowToGet(v: String) {
        infoState = infoState.copy(howToGet = v)
    }

    fun updateStartDate(date: LocalDate) {
        infoState = infoState.copy(startDate = date)
    }

    fun updateEndDate(date: LocalDate) {
        infoState = infoState.copy(endDate = date)
    }

    fun updateStartTime(time: LocalTime) {
        infoState = infoState.copy(startTime = time)
    }


    fun updateEndTime(time: LocalTime) {
        infoState = infoState.copy(endTime = time)
    }

    // ---------- SETTINGS PAGE UPDATES ----------
    fun setIsClosed(v: Boolean) {
        settingsState = settingsState.copy(isClosed = v)
    }

    fun updateLimits(v: String) {
        settingsState = settingsState.copy(limits = v)
    }

    fun updateParticipantsLimits(v: Int?) {
        settingsState = settingsState.copy(participantsLimit = v)
    }

    fun setRequiresRegistration(v: Boolean) {
        settingsState = settingsState.copy(requiresRegistration = v)
    }

    fun toggleRegistrationField(field: String) {
        val newMap = settingsState.registrationFields.toMutableMap()
        newMap[field] = !(newMap[field] ?: false)
        settingsState = settingsState.copy(registrationFields = newMap)
    }

    fun onDraftSave() {
        val event = convertStateToEvent(EventStatus.DRAFT)
        eventRepository.createEvent(event) { }
    }

    fun onPublication() {
        val event = convertStateToEvent(EventStatus.PUBLISHED)
        eventRepository.createEvent(event) { }
    }

    private fun convertStateToEvent(status: EventStatus): Event {
        return Event(
            category = listOf(EventCategory.PLACEHOLDER),
            title = infoState.title,
            content = infoState.description,
            startDate = LocalDateTime.of(infoState.startDate, infoState.startTime),
            endDate = LocalDateTime.of(infoState.endDate, infoState.endTime),
            location = infoState.address,
            posterUrl = "",
            organizerId = UUID.fromString(infoState.organizerId),
            organizationId = null,
            updatedAt = LocalDateTime.now(),
            address = infoState.address,
            route = infoState.howToGet,
            city = infoState.city,
            peopleLimit = settingsState.participantsLimit
                ?: Int.MAX_VALUE,
            registerEndDateTime = LocalDateTime.of(infoState.startDate, infoState.startTime),
            withRegister = settingsState.requiresRegistration,
            open = !settingsState.isClosed, //the difference with is Closed?
            price = null, //no such parameter in creation
            duration = Duration.between(
                LocalDateTime.of(infoState.endDate, infoState.endTime),
                LocalDateTime.of(infoState.startDate, infoState.startTime)
            ).toMinutes().toInt(),
            organizer = null, //???
            isUserParticipating = false,
            eventStatus = status,
            isPublic = !settingsState.isClosed, //the difference with is Closed?
            isFinished = LocalDateTime.of(
                infoState.endDate,
                infoState.endTime
            ) < LocalDateTime.now()
        )
    }
}

data class EventInfoState(
    val organizerId: String? = null,
    val title: String = "",
    val description: String = "",
    val city: String = "",
    val address: String = "",

    val howToGet: String = "",
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val startTime: LocalTime = LocalTime.now(),
    val endTime: LocalTime = LocalTime.now(),
)


data class EventSettingsState(
    val isClosed: Boolean = false,
    val limits: String? = null,
    val participantsLimit: Int? = null,
    val requiresRegistration: Boolean = false,
    val registrationFields: Map<String, Boolean> = mapOf(
        "Имя" to false,
        "Фамилия" to false,
        "Телефон" to false,
        "Почта" to false
    ),
)

