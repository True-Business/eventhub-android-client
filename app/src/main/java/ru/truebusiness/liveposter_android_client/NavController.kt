package ru.truebusiness.liveposter_android_client

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.truebusiness.liveposter_android_client.data.User
import ru.truebusiness.liveposter_android_client.repository.EventRepository
import ru.truebusiness.liveposter_android_client.repository.OrgRepository
import ru.truebusiness.liveposter_android_client.view.EventDetailsPage
import ru.truebusiness.liveposter_android_client.view.FriendsPage
import ru.truebusiness.liveposter_android_client.view.MainPage
import ru.truebusiness.liveposter_android_client.view.SearchPage
import ru.truebusiness.liveposter_android_client.view.registration.RegistrationPage
import ru.truebusiness.liveposter_android_client.view.registration.RegistrationPageEmailVerification
import ru.truebusiness.liveposter_android_client.view.registration.RegistrationPagePersonalInfo
import ru.truebusiness.liveposter_android_client.view.registration.RegistrationPageUserInterest
import ru.truebusiness.liveposter_android_client.view.WelcomePage
import ru.truebusiness.liveposter_android_client.view.organizations.AdminsScreen
import ru.truebusiness.liveposter_android_client.view.organizations.OrganizationPage
import ru.truebusiness.liveposter_android_client.view.viewmodel.AuthViewModel
import ru.truebusiness.liveposter_android_client.view.viewmodel.OrganizationViewModel
import java.util.UUID
import kotlin.uuid.Uuid

@Composable
fun AppNavigation(
    //TODO uncomment
    //authViewModel: AuthViewModel
) {

    val orgViewModel: OrganizationViewModel = viewModel()

    val navController = rememberNavController()
    val repository = EventRepository()
    val orgRepository = OrgRepository()

    // Проверяем авторизацию при старте приложения
//    LaunchedEffect(Unit) {
//        authViewModel.isLoggedIn.collect { isLoggedIn ->
//            if (isLoggedIn) {
//                navController.navigate("main") {
//                    popUpTo(navController.graph.startDestinationId) {
//                        inclusive = true
//                    }
//                }
//            }
//        }
//    }

    NavHost(navController = navController, startDestination = "welcome") {
//        composable("welcome") {
//            WelcomePage(navController, authViewModel)
//        }

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

        composable(
            route = "welcome",
            //arguments = listOf(navArgument("orgId") { type = NavType.StringType })
        ) { backStackEntry ->
            //val orgString = backStackEntry.arguments?.getString("orgId")
            //val uuid = UUID.fromString(orgString)
            val org = orgRepository.fetchOrganization(UUID.randomUUID())
            if (org != null) {
                orgViewModel.setCurrentOrganization(org)
                OrganizationPage(
                    org = org,
                    navController
                )
            }

        }

        composable(
            route = "organizationAdmins",
        ) { backStackEntry ->
            val organization by orgViewModel.currentOrganization.collectAsState()

            val admins = organization?.admins

            AdminsScreen(
                adminsState = organization?.admins ?: emptyList(),
                navController,
                onSaveEditing = ({ users ->
                    orgViewModel.updateAdmins(users)
                })
            )
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