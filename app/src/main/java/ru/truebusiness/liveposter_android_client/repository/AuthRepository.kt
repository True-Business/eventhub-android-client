package ru.truebusiness.liveposter_android_client.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.truebusiness.liveposter_android_client.data.dto.RegistrationResponseDto
import ru.truebusiness.liveposter_android_client.data.dto.RegistrationStatus
import ru.truebusiness.liveposter_android_client.data.dto.UserCredentialsRegistrationDto
import ru.truebusiness.liveposter_android_client.data.dto.UserInfoRegistrationDto
import ru.truebusiness.liveposter_android_client.repository.api.AuthApi
import kotlin.let

class AuthRepository(
    private val authApi: AuthApi,
    private val dataStore: DataStore<Preferences>
) {
    companion object Keys {
        val EMAIL = stringPreferencesKey("email")
        val PASSWORD = stringPreferencesKey("password")
        val USER_ID = stringPreferencesKey("user_id")
        val USERNAME = stringPreferencesKey("username")
        val SHORT_ID = stringPreferencesKey("short_id")
        val REG_STATUS = stringPreferencesKey("reg_status")
        val REG_DATE = stringPreferencesKey("reg_date")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data.map { it[IS_LOGGED_IN] ?: false }
    val email: Flow<String?> = dataStore.data.map { it[EMAIL] }
    suspend fun preRegister(email: String, password: String): RegistrationResponseDto {
         return authApi.preRegister(UserCredentialsRegistrationDto(email, password))
    }

    suspend fun sendCode(userId: String) {
        authApi.sendConfirmationCode(userId)
    }

    suspend fun verifyCode(code: String): RegistrationResponseDto {
        val response = authApi.verifyConfirmationCode(code)
        dataStore.edit { prefs ->
            response.id?.let { prefs[USER_ID] = it }
            prefs[REG_STATUS] = response.status
            response.registrationDate?.let { prefs[REG_DATE] = it }
            if (response.status.equals(RegistrationStatus.SUCCESS.name, ignoreCase = true)) {
                prefs[IS_LOGGED_IN] = true
            }
        }
        return response
    }

    suspend fun postRegister(
        id: String,
        username: String,
        shortId: String,
        email: String?,
        password: String?
    ): RegistrationResponseDto {
        val response = authApi.postRegister(UserInfoRegistrationDto(id, username, shortId))

        dataStore.edit { prefs ->
            response.id?.let { prefs[USER_ID] = it }
            email?.let { prefs[EMAIL] = it }
            password?.let { prefs[PASSWORD] = it }
            prefs[USERNAME] = username
            prefs[SHORT_ID] = shortId
            prefs[REG_STATUS] = response.status
            response.registrationDate?.let { prefs[REG_DATE] = it }
            if (response.status.equals(RegistrationStatus.SUCCESS.name, ignoreCase = true)) {
                prefs[IS_LOGGED_IN] = true
            }
        }
        return response
    }

    suspend fun saveCredentials(email: String, password: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL] = email
            preferences[PASSWORD] = password
        }
    }

    suspend fun logout() {
        dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = false
        }
    }
}
