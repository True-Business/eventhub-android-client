package ru.truebusiness.liveposter_android_client

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.datastore.preferences.preferencesDataStore
import ru.truebusiness.liveposter_android_client.repository.AuthRepository
import ru.truebusiness.liveposter_android_client.view.viewmodel.AuthViewModel
import ru.truebusiness.liveposter_android_client.repository.mocks.mockOrganization
import ru.truebusiness.liveposter_android_client.view.organizations.OrganizationPage


class MainActivity: ComponentActivity() {

    private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val authRepository = AuthRepository(dataStore)
        val authViewModel = AuthViewModel(authRepository)

        setContent {
          //AppNavigation(authViewModel)
            AppNavigation()
            //OrganizationPage(mockOrganization)
        }
    }
}