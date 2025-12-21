package ru.truebusiness.liveposter_android_client.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import ru.truebusiness.liveposter_android_client.view.components.AppNavigationBar
import ru.truebusiness.liveposter_android_client.view.components.InfoSurface
import ru.truebusiness.liveposter_android_client.view.viewmodel.ProfileSettingsViewModel

@Composable
fun ProfileSettingsPage(
    navController: NavController,
    profileSettingsViewModel: ProfileSettingsViewModel = viewModel()
) {
    val state by profileSettingsViewModel.uiState
    val orange = Color(0xFFFF6600)

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
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { navController.navigate("profile") },
                    contentAlignment = Alignment.Center
                ) {
                    if (state.avatarUrl.isNotEmpty()) {
                        AsyncImage(
                            model = state.avatarUrl,
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize(),
                            tint = orange
                        )
                    }
                }


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
                            .clickable(onClick = { profileSettingsViewModel.logout() })
                    ) {
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

