package ru.truebusiness.liveposter_android_client.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepository(private val dataStore: DataStore<Preferences>) {

    companion object {
        val EMAIL_KEY = stringPreferencesKey("email")
        val PASSWORD_KEY = stringPreferencesKey("password")
    }

    /**
     * Получить сохраненный email
     */
    val email: Flow<String?> = dataStore.data
        .map { preferences -> preferences[EMAIL_KEY] }

    /**
     * Получить сохраненный пароль
     */
    val password: Flow<String?> = dataStore.data
        .map { preferences -> preferences[PASSWORD_KEY] }

    /**
     * Сохранить email и пароль
     */
    suspend fun saveCredentials(email: String, password: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
            preferences[PASSWORD_KEY] = password
        }
    }

    /**
     * Очистить данные (для выхода)
     */
    suspend fun clearCredentials() {
        dataStore.edit { preferences ->
            preferences.remove(EMAIL_KEY)
            preferences.remove(PASSWORD_KEY)
        }
    }
}
