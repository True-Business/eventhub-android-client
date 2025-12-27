package ru.truebusiness.liveposter_android_client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
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
import ru.truebusiness.liveposter_android_client.view.test.StorageTestScreen
import ru.truebusiness.liveposter_android_client.view.viewmodel.AuthViewModel
import ru.truebusiness.liveposter_android_client.view.viewmodel.EventCreationViewModel
import ru.truebusiness.liveposter_android_client.view.viewmodel.EventCreationViewModelFactory
import ru.truebusiness.liveposter_android_client.view.viewmodel.EventsViewModel
import ru.truebusiness.liveposter_android_client.view.viewmodel.EventDetailsViewModel
import ru.truebusiness.liveposter_android_client.view.viewmodel.OrganizationViewModel
import ru.truebusiness.liveposter_android_client.view.viewmodel.OrganizationsListViewModel
import ru.truebusiness.liveposter_android_client.view.viewmodel.ProfileSettingsViewModel
import ru.truebusiness.liveposter_android_client.view.viewmodel.ProfileSettingsViewModelFactory
import ru.truebusiness.liveposter_android_client.view.viewmodel.ProfileViewModel
import ru.truebusiness.liveposter_android_client.view.viewmodel.ProfileViewModelFactory
import java.util.UUID

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel
) {
    // Получаем nullable состояние авторизации (null = ещё загружается из DataStore)
    val isLoggedIn by authViewModel.isLoggedInNullable.collectAsState()

    // Флаг таймаута загрузки
    var loadingTimedOut by remember { mutableStateOf(false) }

    // Таймаут 5 секунд для загрузки состояния авторизации
    LaunchedEffect(Unit) {
        delay(5000)
        if (isLoggedIn == null) {
            loadingTimedOut = true
        }
    }

    // Показываем загрузку пока данные не готовы (с таймаутом)
    if (isLoggedIn == null && !loadingTimedOut) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFFFF6600))
        }
        return
    }

    // Если таймаут истёк и данные не загружены - считаем пользователя не авторизованным
    val effectiveIsLoggedIn = isLoggedIn ?: false

    val orgViewModel: OrganizationViewModel = viewModel()
    val eventsViewModel: EventsViewModel = viewModel()
    eventsViewModel.initialize()
    val organizationsListViewModel: OrganizationsListViewModel = viewModel()

    // Получаем AuthRepository для передачи в ViewModels профиля
    val authRepository = authViewModel.getAuthRepository()
    val profileViewModelFactory = ProfileViewModelFactory(authRepository)
    val profileSettingsViewModelFactory = ProfileSettingsViewModelFactory(authRepository)
    val eventCreationViewModelFactory = EventCreationViewModelFactory(authRepository)

    val navController = rememberNavController()

    // TODO: После тестирования Storage API раскомментировать строку ниже и удалить "storage-test"
    // Определяем начальный экран на основе загруженных данных (или таймаута)
    val startDestination = if (effectiveIsLoggedIn) "main" else "welcome"

    // Временно для тестирования Storage API
    // val startDestination = "storage-test"

    // Флаг для пропуска первого LaunchedEffect (начальное значение уже учтено в startDestination)
    var isInitialized by remember { mutableStateOf(false) }

    // Отслеживаем изменения авторизации для навигации (только после инициализации)
    LaunchedEffect(effectiveIsLoggedIn) {
        if (!isInitialized) {
            isInitialized = true
            return@LaunchedEffect
        }

        if (effectiveIsLoggedIn) {
            // При login переходим на main
            navController.navigate("main") {
                popUpTo(0) { inclusive = true }
            }
        } else {
            // При logout возвращаемся на welcome
            navController.navigate("welcome") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {
        // Тестовый экран Storage API (временно)
        composable("storage-test") {
            StorageTestScreen()
        }

        composable("welcome") {
            WelcomePage(navController, authViewModel)
        }

        composable("main") {
            MainPage(navController, authViewModel)
        }

        composable("event-creation") {
            val eventCreationViewModel: EventCreationViewModel =
                viewModel(factory = eventCreationViewModelFactory)
            EventCreationPage(navController, eventCreationViewModel)
        }

        composable(
            route = "event/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable
            val currentUser by authViewModel.currentUser.collectAsState()
            val eventViewModel: EventDetailsViewModel = viewModel()
            LaunchedEffect(eventId) {
                eventViewModel.loadEvent(eventId)
            }

            EventDetailsPage(
                viewModel = eventViewModel,
                onBack = { navController.popBackStack() },
                currentUserId = currentUser?.id
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
        ) {
            AdminsScreen(
                orgViewModel,
                navController,
                onSaveEditing = ({ users ->
                    orgViewModel.updateAdmins(users)
                })
            )
        }

        composable("profile-settings") {
            val profileSettingsViewModel: ProfileSettingsViewModel =
                viewModel(factory = profileSettingsViewModelFactory)
            ProfileSettingsPage(navController, profileSettingsViewModel)
        }
        composable("profile") {
            val profileViewModel: ProfileViewModel = viewModel(factory = profileViewModelFactory)
            ProfilePage(navController, profileViewModel)
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
