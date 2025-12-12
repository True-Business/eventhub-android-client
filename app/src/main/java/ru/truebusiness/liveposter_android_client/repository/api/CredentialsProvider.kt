package ru.truebusiness.liveposter_android_client.repository.api

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * Провайдер учетных данных для Basic Auth.
 * Получает email и password из DataStore, сохраненные при входе пользователя.
 */
class CredentialsProvider(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val EMAIL = stringPreferencesKey("email")
        private val PASSWORD = stringPreferencesKey("password")
    }

    /**
     * Получает текущие учетные данные синхронно.
     * Возвращает Pair(email, password) или null если пользователь не авторизован.
     */
    fun getCredentials(): Pair<String, String>? {
        return runBlocking {
            val prefs = dataStore.data.first()
            val email = prefs[EMAIL]
            val password = prefs[PASSWORD]
            if (email != null && password != null) {
                Pair(email, password)
            } else {
                null
            }
        }
    }
}

