package ru.truebusiness.liveposter_android_client.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.truebusiness.liveposter_android_client.data.Organization
import ru.truebusiness.liveposter_android_client.repository.OrgRepository
import ru.truebusiness.liveposter_android_client.view.organizationslist.OrganizationTab

private const val PAGE_SIZE = 20

data class OrganizationsUiState(
    val items: List<Organization> = emptyList(),
    val isInitialLoading: Boolean = false,
    val isPaging: Boolean = false,
    val endReached: Boolean = false,
    val error: String? = null,
    val selectedTab: OrganizationTab = OrganizationTab.ALL
)

class OrganizationsViewModel(
    private val orgRepository: OrgRepository = OrgRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(OrganizationsUiState(isInitialLoading = true))
    val state: StateFlow<OrganizationsUiState> = _state.asStateFlow()

    private var currentPage = 0
    private var isLoading = false

    init { loadFirstPage() }

    fun onTabSelected(tab: OrganizationTab) {
        if (tab == _state.value.selectedTab) return
        _state.update { it.copy(selectedTab = tab) }
        loadFirstPage()
    }

    fun loadFirstPage() {
        if (isLoading) return
        isLoading = true
        currentPage = 0
        _state.update { it.copy(isInitialLoading = true, error = null, endReached = false) }

        viewModelScope.launch {
            runCatching {
                orgRepository.fetchOrganizationsPage(
                    page = currentPage,
                    pageSize = PAGE_SIZE,
                    tab = _state.value.selectedTab
                )
            }.onSuccess { page ->
                isLoading = false
                _state.update {
                    it.copy(
                        items = page,
                        isInitialLoading = false,
                        endReached = page.size < PAGE_SIZE
                    )
                }
            }.onFailure { e ->
                isLoading = false
                _state.update { it.copy(isInitialLoading = false, error = e.message ?: "Не удалось загрузить") }
            }
        }
    }

    fun loadNextPage() {
        val s = _state.value
        if (isLoading || s.endReached) return
        isLoading = true
        _state.update { it.copy(isPaging = true, error = null) }

        viewModelScope.launch {
            runCatching {
                val next = currentPage + 1
                orgRepository.fetchOrganizationsPage(
                    page = next,
                    pageSize = PAGE_SIZE,
                    tab = _state.value.selectedTab
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
                _state.update { it.copy(isPaging = false, error = e.message ?: "Ошибка") }
            }
        }
    }
}