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
import ru.truebusiness.liveposter_android_client.repository.OrgRepository


class OrganizationViewModel : ViewModel() {
    private val TAG = "ORGANIZATION_VIEW_MODEL"

    private val repository = OrgRepository()


    private var _currentOrganization = MutableStateFlow<Organization?>(null)
    var currentOrganization: StateFlow<Organization?> = _currentOrganization

    //TODO update this to check if user is admin
    var isMy: StateFlow<Boolean> = MutableStateFlow(true)

    fun setCurrentOrganization(organization: Organization) {
        _currentOrganization.value = organization
        Log.d(TAG, "current organization: ${organization.name}")
    }

    fun updateAdmins(newAdmins: List<User>) {
        _currentOrganization.update { org ->
            org?.copy(admins = newAdmins)
        }
        Log.d(TAG, "admins count: ${newAdmins.size}")
    }


}