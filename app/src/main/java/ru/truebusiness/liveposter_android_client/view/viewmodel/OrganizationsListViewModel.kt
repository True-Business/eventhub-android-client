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
    val error: String? = null,
    val selectedTab: OrganizationTab = OrganizationTab.ALL
)

class OrganizationsListViewModel(
    private val orgRepository: OrgRepository = OrgRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(OrganizationsUiState(isInitialLoading = true))
    val state: StateFlow<OrganizationsUiState> = _state.asStateFlow()

    init {
        loadOrganizations()
    }

    fun onTabSelected(tab: OrganizationTab) {
        if (tab == _state.value.selectedTab) return
        _state.update { it.copy(selectedTab = tab) }
        loadOrganizations()
    }

    fun loadOrganizations() {
        _state.update { it.copy(isInitialLoading = true, error = null) }
        viewModelScope.launch {
            val (onlySubscribed, onlyAdministrated) = when (_state.value.selectedTab) {
                OrganizationTab.ALL -> Pair(false, false)
                OrganizationTab.SUBSCRIPTIONS -> Pair(true, false)
                OrganizationTab.MINE -> Pair(false, true)
            }

            val result = orgRepository.searchOrganizations(
                onlySubscribed = onlySubscribed,
                onlyAdministrated = onlyAdministrated
            )

            if (result != null) {
                _state.update {
                    it.copy(
                        items = result,
                        isInitialLoading = false
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isInitialLoading = false,
                        error = "Failed to load organizations"
                    )
                }
            }
        }
    }
}