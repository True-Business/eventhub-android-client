package ru.truebusiness.liveposter_android_client.view.events

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.ui.theme.ChipBackground
import ru.truebusiness.liveposter_android_client.ui.theme.pageGradient
import ru.truebusiness.liveposter_android_client.view.components.*
import java.util.*

enum class EventCategory(val displayName: String) {
    DRAFTS("Черновики"),
    COMPLETED("Завершенные"),
    SCHEDULED("Запланированные")
}

enum class VisitsCategory(val displayName: String) {
    WILLGO("Я пойду"),
    VISITED("Посещенные")
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    events: List<Event>

    ) {
    var mainTab by remember { mutableStateOf(MainTab.VISITS) }
    var showFilter by remember { mutableStateOf(false) }
    var filterState by remember { mutableStateOf(FilterState()) }
    var selectedSubcategory by remember { mutableStateOf(0) }
    val density = LocalDensity.current
    var subcategoryButtonsHeight by remember { mutableStateOf(0.dp) }


    val gridState = rememberLazyGridState()
    val isScrolled by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 0 || gridState.firstVisibleItemScrollOffset > 0
        }
    }

    val subcategoryLabels = when (mainTab) {
        MainTab.VISITS -> listOf(
            VisitsCategory.WILLGO.displayName,
            VisitsCategory.VISITED.displayName
        )

        MainTab.EVENTS -> listOf(
            EventCategory.DRAFTS.displayName,
            EventCategory.COMPLETED.displayName,
            EventCategory.SCHEDULED.displayName
        )
    }

    val topPadding by animateDpAsState(
        targetValue = if (isScrolled) 0.dp else subcategoryButtonsHeight + 12.dp
    )
    val cornerRadius by animateDpAsState(targetValue = if (isScrolled) 0.dp else 24.dp)

    LaunchedEffect(mainTab) {
        selectedSubcategory = 0
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = pageGradient)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppTopBar(
                onBack = {
                    navController.popBackStack()
                },
                onFilter = { showFilter = true },
                titleContent = {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        item {
                            AnimatedSelectionChip(
                                text = "Посещения",
                                selected = mainTab == MainTab.VISITS,
                                onClick = { mainTab = MainTab.VISITS })
                        }
                        item {
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                        item {
                            AnimatedSelectionChip(
                                text = "Мероприятия",
                                selected = mainTab == MainTab.EVENTS,
                                onClick = { mainTab = MainTab.EVENTS }
                            )
                        }
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

            Box(modifier = Modifier.fillMaxSize()) {
                AnimatedContent(
                    targetState = subcategoryLabels
                ) { targetState ->
                    SubcategoryButtons(
                        labels = targetState,
                        selectedIndex = selectedSubcategory,
                        onSelect = { selectedSubcategory = it },
                        modifier = Modifier.onGloballyPositioned { coordinates ->
                            subcategoryButtonsHeight =
                                with(density) { coordinates.size.height.toDp() }
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = topPadding)
                        .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius))
                        .background(Color.White)
                ) {
                    LazyVerticalGrid(
                        state = gridState,
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(events) { event ->
                            TinyEventCard(event = event)
                        }
                    }
                }
            }
        }



        AnimatedVisibility(
            visible = mainTab == MainTab.EVENTS && subcategoryLabels.getOrNull(selectedSubcategory) == EventCategory.DRAFTS.displayName,
            enter = scaleIn(),
            exit = scaleOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(horizontal = 24.dp, vertical = 24.dp),
        ) {
            FloatingActionButton(
                onClick = {
                    //TODO navigate to create event
                },
                shape = CircleShape,
                containerColor = ChipBackground
            ) {
                Icon(Icons.Filled.Add, "create event")
            }
        }
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
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.50f))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back"
                )
            }
        },
        actions = {
            IconButton(
                onClick = onFilter,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.50f))
            ) {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = "filter",
                    tint = Color.Black
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

