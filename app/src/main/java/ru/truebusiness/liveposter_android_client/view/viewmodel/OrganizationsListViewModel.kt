package ru.truebusiness.liveposter_android_client.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.truebusiness.liveposter_android_client.data.Organization
import ru.truebusiness.liveposter_android_client.repository.OrgRepository

private const val PAGE_SIZE = 20
private const val SEARCH_DEBOUNCE_MS = 400L

data class OrganizationsUiState(
    val items: List<Organization> = emptyList(),
    val isInitialLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isPaging: Boolean = false,
    val endReached: Boolean = false,
    val query: String = "",
    val errorMessage: String? = null
)

sealed interface OrganizationsUiEvent {
    data class OpenOrganization(val id: java.util.UUID) : OrganizationsUiEvent
}

class OrganizationsViewModel(
    private val repo: OrgRepository = OrgRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(OrganizationsUiState(isInitialLoading = true))
    val state: StateFlow<OrganizationsUiState> = _state.asStateFlow()

    private val _events = Channel<OrganizationsUiEvent>(Channel.BUFFERED)
    val events: Flow<OrganizationsUiEvent> = _events.receiveAsFlow()

    // внутреннее состояние пагинации
    private var currentPage = 0
    private var isLoading = false

    // поток поисковых запросов для дебаунса
    private val searchQuery = MutableStateFlow("")

    init {
        // стартовая загрузка
        refresh(initial = true)

        // дебаунсим поиск и перезагружаем список
        viewModelScope.launch {
            searchQuery
                .debounce(SEARCH_DEBOUNCE_MS)
                .distinctUntilChanged()
                .collect { q ->
                    _state.update { it.copy(query = q) }
                    refresh()
                }
        }
    }

    fun onSearchChange(newValue: String) {
        searchQuery.value = newValue
    }

    fun onOrganizationClick(item: Organization) {
        viewModelScope.launch {
            _events.send(OrganizationsUiEvent.OpenOrganization(item.id))
        }
    }

    /** Pull-to-refresh или реакция на поиск */
    fun refresh(initial: Boolean = false) {
        if (isLoading) return
        isLoading = true
        currentPage = 0

        _state.update {
            it.copy(
                isInitialLoading = initial,
                isRefreshing = !initial,
                isPaging = false,
                endReached = false,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            runCatching {
                repo.fetchOrganizationsPage(
                    page = currentPage,
                    pageSize = PAGE_SIZE,
                    query = _state.value.query.ifBlank { null }
                )
            }.onSuccess { firstPage ->
                isLoading = false
                _state.update {
                    it.copy(
                        items = firstPage,
                        isInitialLoading = false,
                        isRefreshing = false,
                        endReached = firstPage.size < PAGE_SIZE,
                        errorMessage = null
                    )
                }
            }.onFailure { e ->
                isLoading = false
                _state.update {
                    it.copy(
                        isInitialLoading = false,
                        isRefreshing = false,
                        errorMessage = e.message ?: "Не удалось загрузить данные"
                    )
                }
            }
        }
    }

    /** Подгрузка следующей страницы при прокрутке */
    fun loadNextPage() {
        val s = _state.value
        if (isLoading || s.endReached) return
        isLoading = true
        _state.update { it.copy(isPaging = true, errorMessage = null) }

        viewModelScope.launch {
            runCatching {
                val nextPage = currentPage + 1
                repo.fetchOrganizationsPage(
                    page = nextPage,
                    pageSize = PAGE_SIZE,
                    query = s.query.ifBlank { null }
                )
            }.onSuccess { page ->
                isLoading = false
                if (page.isNotEmpty()) currentPage += 1
                _state.update {
                    it.copy(
                        items = it.items + page,
                        isPaging = false,
                        endReached = page.size < PAGE_SIZE
                    )
                }
            }.onFailure { e ->
                isLoading = false
                _state.update {
                    it.copy(
                        isPaging = false,
                        errorMessage = e.message ?: "Ошибка подгрузки страницы"
                    )
                }
            }
        }
    }
}