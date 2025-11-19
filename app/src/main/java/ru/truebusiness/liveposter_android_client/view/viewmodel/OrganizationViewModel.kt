package ru.truebusiness.liveposter_android_client.view.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.truebusiness.liveposter_android_client.data.Organization
import ru.truebusiness.liveposter_android_client.data.User
import ru.truebusiness.liveposter_android_client.data.emptyOrganization
import ru.truebusiness.liveposter_android_client.repository.OrgRepository
import java.util.UUID
import kotlin.uuid.Uuid


class OrganizationViewModel : ViewModel() {

    sealed class OrganizationState {
        object Loading : OrganizationState()
        data class Success(val org: Organization) : OrganizationState()
        data class Error(val message: String) : OrganizationState()
    }

    private val _organizationState = MutableStateFlow<OrganizationState>(OrganizationState.Loading)
    val organizationState: StateFlow<OrganizationState> get() = _organizationState

    private val TAG = "ORGANIZATION_VIEW_MODEL"

    private val repository = OrgRepository()


    private var _currentOrganization = MutableStateFlow(emptyOrganization())
    var currentOrganization: StateFlow<Organization> = _currentOrganization

    //TODO update this to check if user is admin
    var isMy: StateFlow<Boolean> = MutableStateFlow(true)

    fun setCurrentOrganization(organization: Organization) {
        _currentOrganization.value = organization
        Log.d(TAG, "current organization: ${organization.name}")
    }

    fun fetchOrganizationFromRepo(orgId: UUID) {
        _organizationState.value = OrganizationState.Loading
        viewModelScope.launch {
            val organization = repository.fetchOrganization(orgId)
            if (organization != null) {
                _organizationState.value = OrganizationState.Success(organization)
                _currentOrganization.value = organization
                Log.d(TAG, "Organization loaded: ${organization.name}")
            } else {
                _organizationState.value = OrganizationState.Error("Organization not found")
                Log.e(TAG, "Failed to load organization with ID: $orgId")
            }
        }
    }

    /**
     * Загружает организацию через mock API (для разработки)
     */
    fun fetchOrganizationFromRepoMock(orgId: UUID) {
        val org = repository.fetchOrganizationMock(orgId)
        if (org != null) {
            _organizationState.value = OrganizationState.Success(org)
            // TODO delete
            _currentOrganization.value = org
        } else {
            _organizationState.value = OrganizationState.Error("organization not found")
        }
    }

    fun updateOrganization(
        name: String,
        description: String,
        address: String,
        admins: List<User>,
        images: List<String>
    ) {
        _currentOrganization.update { org ->
            org.copy(
                name = name,
                description = description,
                address = address,
                admins = admins,
                images = images
            )
        }
    }

    /**
     * Сохраняет изменения организации через API
     */
    fun saveOrganizationChanges(onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            val currentOrg = _currentOrganization.value
            val updatedOrg = repository.updateOrganization(currentOrg.id, currentOrg)
            if (updatedOrg != null) {
                _currentOrganization.value = updatedOrg
                Log.d(TAG, "Organization updated successfully: ${updatedOrg.name}")
                onSuccess()
            } else {
                val errorMsg = "Failed to update organization"
                Log.e(TAG, errorMsg)
                onError(errorMsg)
            }
        }
    }

    fun updateAdmins(newAdmins: List<User>) {
        _currentOrganization.update { org ->
            org.copy(admins = newAdmins)
        }
        Log.d(TAG, "admins count: ${newAdmins.size}")
    }


}