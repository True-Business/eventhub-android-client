package ru.truebusiness.liveposter_android_client

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.truebusiness.liveposter_android_client.repository.EventRepository
import ru.truebusiness.liveposter_android_client.view.EventDetailsPage
import ru.truebusiness.liveposter_android_client.view.MainPage
import ru.truebusiness.liveposter_android_client.view.registration.RegistrationPage
import ru.truebusiness.liveposter_android_client.view.registration.RegistrationPageEmailVerification
import ru.truebusiness.liveposter_android_client.view.registration.RegistrationPagePersonalInfo
import ru.truebusiness.liveposter_android_client.view.registration.RegistrationPageUserInterest
import ru.truebusiness.liveposter_android_client.view.WelcomePage

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val repository = EventRepository()

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomePage(navController)
        }

        composable("main") {
            MainPage(navController)
        }

        composable(
            route = "event/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) {
            val arg = it.arguments?.getString("eventId")
            val event = repository.fetchEventMock(arg!!)

            if (event != null) {
                EventDetailsPage(event) {
                    navController.popBackStack()
                }
            }
        }

        /* Блок регистрации нового пользователя */
        composable(
            route = "registration"
        ) {
            RegistrationPage(navController)
        }

        composable(
            route = "email_verification/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) {
            val email = it.arguments?.getString("email") ?: ""
            RegistrationPageEmailVerification(
                navController = navController,
                email = email
            )
        }

        composable(route = "user_personal_data") {
            RegistrationPagePersonalInfo(navController)
        }

        composable(route = "user_personal_info") {
            RegistrationPageUserInterest(navController)
        }

        /****************************************/
    }
}