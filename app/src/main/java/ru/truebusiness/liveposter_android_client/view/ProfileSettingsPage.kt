package ru.truebusiness.liveposter_android_client.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.truebusiness.liveposter_android_client.view.components.AppNavigationBar
import ru.truebusiness.liveposter_android_client.view.components.InfoSurface
import ru.truebusiness.liveposter_android_client.view.components.ProfileAvatar
import ru.truebusiness.liveposter_android_client.view.viewmodel.ProfileSettingsViewModel

@Composable
fun ProfileSettingsPage(
    navController: NavController,
    profileSettingsViewModel: ProfileSettingsViewModel = viewModel()
) {
    val state by profileSettingsViewModel.uiState
    val orange = Color(0xFFFF6600)
    val snackbarHostState = remember { SnackbarHostState() }

    // Показываем ошибку удаления в Snackbar
    LaunchedEffect(state.deleteError) {
        state.deleteError?.let { error ->
            snackbarHostState.showSnackbar(error)
            profileSettingsViewModel.clearDeleteError()
        }
    }

    // Диалог подтверждения удаления аккаунта
    if (state.showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { profileSettingsViewModel.hideDeleteConfirmation() },
            title = { Text("Удаление аккаунта") },
            text = { Text("Вы уверены, что хотите удалить аккаунт? Это действие нельзя отменить.") },
            confirmButton = {
                TextButton(
                    onClick = { profileSettingsViewModel.deleteAccount() }
                ) {
                    Text("Удалить", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { profileSettingsViewModel.hideDeleteConfirmation() }
                ) {
                    Text("Отмена")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    0f to orange,
                    0.44f to Color.White,
                    1f to Color.White
                )
            )
    ) {
        Scaffold(
            bottomBar = {
                AppNavigationBar(
                    navController,
                    selectedRoute = "profile-settings"
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                }
            },
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Аватар
                ProfileAvatar(
                    avatarUrl = state.avatarUrl,
                    size = 100.dp,
                    onClick = { navController.navigate("profile") }
                )


                Spacer(modifier = Modifier.height(20.dp))

                Text(text = state.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = state.username, fontSize = 20.sp)

                Spacer(modifier = Modifier.height(24.dp))

                // "Мой профиль"
                InfoSurface(
                    modifier = Modifier
                        .clickable { navController.navigate("profile") },
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Мой профиль",
                            fontSize = 20.sp
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color(0xFFFF6600),
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Кнопки выхода и удаления
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InfoSurface(
                        backgroundColor = orange,
                        modifier = Modifier
                            .clickable(onClick = { profileSettingsViewModel.logout() })
                    ) {
                        Text(
                            text = "Выход из аккаунта",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoSurface(
                        backgroundColor = orange,
                        modifier = Modifier
                            .clickable(
                                enabled = !state.isDeleting,
                                onClick = { profileSettingsViewModel.showDeleteConfirmation() }
                            )
                    ) {
                        if (state.isDeleting) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "Удалить аккаунт",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
