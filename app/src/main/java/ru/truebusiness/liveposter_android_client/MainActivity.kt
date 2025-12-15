package ru.truebusiness.liveposter_android_client

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.datastore.preferences.preferencesDataStore
import ru.truebusiness.liveposter_android_client.repository.AuthRepository
import ru.truebusiness.liveposter_android_client.repository.api.CredentialsProvider
import ru.truebusiness.liveposter_android_client.repository.api.RetrofitInstance
import ru.truebusiness.liveposter_android_client.view.viewmodel.AuthViewModel


class MainActivity: ComponentActivity() {

    private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Инициализируем RetrofitInstance с провайдером учетных данных из DataStore
        val credentialsProvider = CredentialsProvider(dataStore)
        RetrofitInstance.initialize(credentialsProvider)

        val api = RetrofitInstance.authApi
        val authRepository = AuthRepository(api, dataStore)
        val authViewModel = AuthViewModel(authRepository)

        setContent {
            AppNavigation(authViewModel)
        }
    }
}