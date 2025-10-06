package ru.truebusiness.liveposter_android_client.view.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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

    fun updateAdmins(newAdmins: List<User>) {
        _currentOrganization.update { org ->
            org.copy(admins = newAdmins)
        }
        Log.d(TAG, "admins count: ${newAdmins.size}")
    }


}