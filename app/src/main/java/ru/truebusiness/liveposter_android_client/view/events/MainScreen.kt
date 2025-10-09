package ru.truebusiness.liveposter_android_client.view.events

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.truebusiness.liveposter_android_client.R
import ru.truebusiness.liveposter_android_client.ui.theme.pageGradient
import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.view.components.*
import java.util.UUID


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    var mainTab by remember { mutableStateOf(MainTab.EVENTS) }
    var showFilter by remember { mutableStateOf(false) }
    var filterState by remember { mutableStateOf(FilterState()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = pageGradient)
    ) {
        AppTopBar(
            onBack = { /* handle back */ },
            onFilter = { showFilter = true },
            titleContent = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TabButton(
                        text = "Посещения",
                        selected = mainTab == MainTab.VISITS,
                        onClick = { mainTab = MainTab.VISITS })
                    Spacer(modifier = Modifier.width(8.dp))
                    TabButton(
                        text = "Мероприятия",
                        selected = mainTab == MainTab.EVENTS,
                        onClick = { mainTab = MainTab.EVENTS })
                }
            }
        )

        if (showFilter) {
            FilterDialog(
                initial = filterState,
                onDismiss = { showFilter = false },
                onApply = { newState ->
                    filterState = newState
                    showFilter = false
                })
        }

        AnimatedContent(
            targetState = mainTab,
        ) { target ->
            when (target) {
                MainTab.VISITS -> VisitsScreen(itemsProvider = {
                    List(12) { i ->
                        Event(
                            id = UUID.randomUUID(),
                            category = emptyList(),
                            title = if (i % 2 == 0) "Научный пикник" else "Интер неделя",
                            content = "Подробности...",
                            startDate = if (i % 2 == 0) "25.07.2025" else "17.04.2025",
                            endDate = "",
                            location = "Онлайн",
                            posterUrl = "https://i.pravatar.cc/300?img=$i"
                        )
                    }
                })

                MainTab.EVENTS -> EventsScreen(itemsProvider = {
                    List(8) { i ->
                        Event(
                            id = UUID.randomUUID(),
                            category = emptyList(),
                            title = if (i % 2 != 0) "Научный пикник" else "Интер неделя",
                            content = "Подробности...",
                            startDate = if (i % 2 != 0) "25.07.2025" else "17.04.2025",
                            endDate = "",
                            location = "Оффлайн",
                            posterUrl = "https://i.pravatar.cc/300?img=${i + 12}"
                        )
                    }
                })
            }
        }
    }
}


@Composable
private fun TabButton(text: String, selected: Boolean, onClick: () -> Unit) {
    val background = if (selected) Color(0xFFFCEEE4) else Color.Transparent
    val textColor = if (selected) Color(0xFF4E260F) else Color.White

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(background)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    titleContent: @Composable RowScope.() -> Unit,
    onBack: () -> Unit,
    onFilter: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Назад", tint = Color.White)
            }
        },
        actions = {
            IconButton(onClick = onFilter) {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = "Фильтр",
                    tint = Color.White
                )
            }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                titleContent()
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

