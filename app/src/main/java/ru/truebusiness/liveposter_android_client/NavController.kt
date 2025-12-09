package ru.truebusiness.liveposter_android_client

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.truebusiness.liveposter_android_client.repository.EventRepository
import ru.truebusiness.liveposter_android_client.view.event.EventDetailsPage
import ru.truebusiness.liveposter_android_client.view.FriendsPage
import ru.truebusiness.liveposter_android_client.view.MainPage
import ru.truebusiness.liveposter_android_client.view.ProfilePage
import ru.truebusiness.liveposter_android_client.view.ProfileSettingsPage
import ru.truebusiness.liveposter_android_client.view.SearchPage
import ru.truebusiness.liveposter_android_client.view.registration.RegistrationPage
import ru.truebusiness.liveposter_android_client.view.registration.RegistrationPageEmailVerification
import ru.truebusiness.liveposter_android_client.view.registration.RegistrationPagePersonalInfo
import ru.truebusiness.liveposter_android_client.view.registration.RegistrationPageUserInterest
import ru.truebusiness.liveposter_android_client.view.WelcomePage
import ru.truebusiness.liveposter_android_client.view.events.EventsMainScreen
import ru.truebusiness.liveposter_android_client.view.events.EventCreationPage
import ru.truebusiness.liveposter_android_client.view.events.EventCreationSettingsPage
import ru.truebusiness.liveposter_android_client.view.organizations.AdminsScreen
import ru.truebusiness.liveposter_android_client.view.organizations.OrganizationPage
import ru.truebusiness.liveposter_android_client.view.organizationslist.OrganizationsListPage
import ru.truebusiness.liveposter_android_client.view.viewmodel.AuthViewModel
import ru.truebusiness.liveposter_android_client.view.viewmodel.EventsViewModel
import ru.truebusiness.liveposter_android_client.view.viewmodel.EventDetailsViewModel
import ru.truebusiness.liveposter_android_client.view.viewmodel.OrganizationViewModel
import ru.truebusiness.liveposter_android_client.view.viewmodel.OrganizationsListViewModel
import java.util.UUID

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel
) {

    val orgViewModel: OrganizationViewModel = viewModel()
    val eventsViewModel: EventsViewModel = viewModel()
    eventsViewModel.initialize()
    val organizationsListViewModel: OrganizationsListViewModel = viewModel()

    val navController = rememberNavController()
    val repository = EventRepository()

    // Проверяем авторизацию при старте приложения
    LaunchedEffect(Unit) {
        authViewModel.isLoggedIn.collect { isLoggedIn ->
            if (isLoggedIn) {
                navController.navigate("main") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomePage(navController, authViewModel)
        }

        composable("main") {
            MainPage(navController)
        }

        composable("event-creation") {
            EventCreationPage(navController)
        }

        composable("event-creation-settings") {
            EventCreationSettingsPage(navController)
        }

        composable(
            route = "event/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable
            val eventViewModel: EventDetailsViewModel = viewModel()
            LaunchedEffect(eventId) {
                eventViewModel.loadEvent(eventId)
            }

            EventDetailsPage(
                viewModel = eventViewModel,
                onBack = { navController.popBackStack() }
            )

        }

        composable(
            route = "organizations",

        ) {
            OrganizationsListPage(organizationsListViewModel, navController)
        }

        composable(
            route = "organizations/{orgId}",
            arguments = listOf(navArgument("orgId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orgString = backStackEntry.arguments?.getString("orgId")
            val uuid = UUID.fromString(orgString)

            OrganizationPage(
                orgViewMod = orgViewModel,
                navigator = navController,
                organizationId = uuid
            )

        }

        composable(
            route = "organizationAdmins",
        ) { backStackEntry ->
            AdminsScreen(
                orgViewModel,
                navController,
                onSaveEditing = ({ users ->
                    orgViewModel.updateAdmins(users)
                })
            )
        }

        composable("profile-settings") {
            ProfileSettingsPage(navController)
        }
        composable("profile") {
            ProfilePage(navController)
        }

        /* Нижний блок навигации по приложению */

        composable(
            route = "search"
        ) {
            SearchPage(navController)
        }

        composable(
            route = "friends"
        ) {
            FriendsPage(navController)
        }

        /***************************************/

        /* Блок регистрации нового пользователя */
        composable(
            route = "registration"
        ) {
            RegistrationPage(authViewModel, navController)
        }

        composable(
            route = "email_verification/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId").orEmpty()
            RegistrationPageEmailVerification(
                viewModel = authViewModel,
                navController = navController,
                userId = userId
            )
        }

        composable(
            route = "user_personal_data/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId").orEmpty()
            RegistrationPagePersonalInfo(
                vm = authViewModel,
                navController = navController,
                userId = userId
            )
        }

        composable(route = "user_personal_info") {
            RegistrationPageUserInterest(navController)
        }

        composable(
            route = "events"
        ) {
            EventsMainScreen(navController, eventsViewModel)
        }

        /****************************************/
    }
}
