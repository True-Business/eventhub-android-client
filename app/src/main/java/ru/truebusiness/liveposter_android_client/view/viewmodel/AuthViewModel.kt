package ru.truebusiness.liveposter_android_client.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.truebusiness.liveposter_android_client.repository.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository): ViewModel() {

    /**
     * Проверка, авторизован ли пользователь
     */
    val isLoggedIn: Flow<Boolean> = authRepository.email.map { it != null }

    /**
     * Вход (сохранение данных)
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.saveCredentials(email, password)
        }
    }

    /**
     * Выход (очистка данных)
     */
    fun logout() {
        viewModelScope.launch {
            authRepository.clearCredentials()
        }
    }
}