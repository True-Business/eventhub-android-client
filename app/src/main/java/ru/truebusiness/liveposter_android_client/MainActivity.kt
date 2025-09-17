package ru.truebusiness.liveposter_android_client

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.datastore.preferences.preferencesDataStore
import ru.truebusiness.liveposter_android_client.repository.AuthRepository
import ru.truebusiness.liveposter_android_client.view.viewmodel.AuthViewModel
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import ru.truebusiness.liveposter_android_client.repository.mocks.mockOrganization
import ru.truebusiness.liveposter_android_client.view.organizations.OrganizationPage


class MainActivity: ComponentActivity() {

    private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

    override fun onCreate(savedInstanceState: Bundle?) {
//        //позволяем приложению рисовать за системными панелями
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        //делаем панели прозрачными (чтобы "занимать" их своим фоном)
//        window.statusBarColor = Color.TRANSPARENT
//        window.navigationBarColor = Color.TRANSPARENT
//
//        //по желанию: светлые/тёмные иконки статуса/навигации
//        val controller = WindowInsetsControllerCompat(window, window.decorView)
//        controller.isAppearanceLightStatusBars = true      // true — тёмные иконки, false — светлые
//        controller.isAppearanceLightNavigationBars = true  // аналогично для навигации

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val authRepository = AuthRepository(dataStore)
        val authViewModel = AuthViewModel(authRepository)

        setContent {
          //AppNavigation(authViewModel)
            OrganizationPage(mockOrganization)
        }
    }
}