package ru.truebusiness.liveposter_android_client.view.components

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import ru.truebusiness.liveposter_android_client.R

@Composable
fun AppNavigationBar(
    navController: NavController,
    selectedRoute: String
) {
    val orange = Color(0xFFFF6600)
    val darkOrange = Color(0xFFE55A00)

    val items = listOf(
        NavItem("main", "Главная", R.drawable.icon_mainpage),
        NavItem("events", "События", R.drawable.icon_events),
        NavItem("friends", "Друзья", R.drawable.icon_friends),
        NavItem("organizations", "Организации", R.drawable.icon_organizations)
    )

    val context = LocalContext.current
    val view = LocalView.current
    LaunchedEffect(Unit) {
        val window = (context as? Activity)?.window
        window?.let {
            WindowCompat.getInsetsController(it, view).isAppearanceLightNavigationBars = false
            it.navigationBarColor = orange.toArgb()
        }
    }

    NavigationBar(
        containerColor = orange,
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(68.dp),
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            val selected = selectedRoute == item.route

            NavigationBarItem(
                selected = selected,
                onClick = { navController.navigate(item.route) },
                icon = {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                            .clip(CircleShape)
                            .background(if (selected) darkOrange else Color.Transparent)
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.label,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = item.label,
                                color = Color.White,
                                fontSize = 8.sp,
                                maxLines = 1
                            )
                        }
                    }
                },
                label = {},
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White
                ),
                interactionSource = remember { MutableInteractionSource() }
            )
        }
    }
}

data class NavItem(val route: String, val label: String, val icon: Int)
