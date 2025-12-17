package ru.truebusiness.liveposter_android_client.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.truebusiness.liveposter_android_client.data.User
import ru.truebusiness.liveposter_android_client.data.dto.RegistrationResponseDto
import ru.truebusiness.liveposter_android_client.data.dto.RegistrationStatus
import ru.truebusiness.liveposter_android_client.data.dto.UserCredentialsRegistrationDto
import ru.truebusiness.liveposter_android_client.data.dto.UserInfoRegistrationDto
import ru.truebusiness.liveposter_android_client.data.toUser
import ru.truebusiness.liveposter_android_client.repository.api.AuthApi

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
        val BIO = stringPreferencesKey("bio")
        val REG_STATUS = stringPreferencesKey("reg_status")
        val REG_DATE = stringPreferencesKey("reg_date")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val CONFIRMED = booleanPreferencesKey("confirmed")
    }

    val email: Flow<String?> = dataStore.data.map { it[EMAIL] }

    // Flow с nullable Boolean: null = ещё не загружено, true/false = реальное значение
    val isLoggedInNullable: Flow<Boolean?> = dataStore.data.map { it[IS_LOGGED_IN] }

    val currentUser: Flow<User?> = dataStore.data.map { prefs ->
        val id = prefs[USER_ID] ?: return@map null
        User(
            id = id,
            username = prefs[USERNAME] ?: prefs[EMAIL] ?: "",
            shortId = prefs[SHORT_ID],
            bio = prefs[BIO],
            registrationDate = prefs[REG_DATE],
            confirmed = prefs[CONFIRMED] ?: false
        )
    }
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

    suspend fun login(email: String, password: String): User {
        val userDto = authApi.login(UserCredentialsRegistrationDto(email, password))

        val user = userDto.toUser()

        dataStore.edit { prefs ->
            prefs[USER_ID] = user.id
            prefs[EMAIL] = email
            prefs[PASSWORD] = password
            prefs[USERNAME] = user.username
            user.shortId?.let { prefs[SHORT_ID] = it }
            user.bio?.let { prefs[BIO] = it }
            user.registrationDate?.let { prefs[REG_DATE] = it }
            prefs[CONFIRMED] = user.confirmed
            prefs[IS_LOGGED_IN] = true
        }

        return user
    }

    suspend fun logout() {
        dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = false
        }
    }

    suspend fun loginAnonymously() {
        dataStore.edit { prefs ->
            prefs[USER_ID] = "anonymous-user-id"
            prefs[USERNAME] = "Василий Попов"
            prefs[SHORT_ID] = "vasily_P"
            prefs[BIO] = "Я Вася Пупкин, студент 2 курса ФИТ НГУ и вот какой я классный. Приходите все на мероприятия НГУ!"
            prefs[CONFIRMED] = true
            prefs[IS_LOGGED_IN] = true
        }
    }

    suspend fun updateUserProfile(username: String? = null, bio: String? = null) {
        dataStore.edit { prefs ->
            username?.let { prefs[USERNAME] = it }
            bio?.let { prefs[BIO] = it }
        }
    }
}
