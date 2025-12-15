package ru.truebusiness.liveposter_android_client.view.events

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ru.truebusiness.liveposter_android_client.data.MainTab
import ru.truebusiness.liveposter_android_client.data.VisitsCategory
import ru.truebusiness.liveposter_android_client.data.EventsCategory
import ru.truebusiness.liveposter_android_client.ui.theme.ChipBackground
import ru.truebusiness.liveposter_android_client.ui.theme.pageGradient
import ru.truebusiness.liveposter_android_client.view.components.*
import ru.truebusiness.liveposter_android_client.view.viewmodel.EventsViewModel

// Legacy enums for compatibility - these are now in data package
enum class EventCategory(val displayName: String) {
    DRAFTS("Черновики"),
    COMPLETED("Завершенные"),
    SCHEDULED("Запланированные")
}

// Legacy enum for compatibility - this is now in data package
enum class VisitsCategoryLegacy(val displayName: String) {
    WILLGO("Я пойду"),
    VISITED("Посещенные")
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EventsMainScreen(
    navController: NavHostController,
    eventsViewModel: EventsViewModel = viewModel()
) {
    // Get state from ViewModel
    val events by eventsViewModel.events.observeAsState(emptyList())
    val isLoading by eventsViewModel.isLoading.observeAsState(false)
    val filterState by eventsViewModel.filterState.observeAsState()
    val currentMainTab by eventsViewModel.currentMainTab.observeAsState(MainTab.VISITS)
    val currentVisitsCategory by eventsViewModel.currentVisitsCategory.observeAsState(VisitsCategory.WILLGO)
    val currentEventsCategory by eventsViewModel.currentEventsCategory.observeAsState(EventsCategory.DRAFTS)

    // Local UI state
    var showFilter by remember { mutableStateOf(false) }
    var selectedSubcategory by remember { mutableStateOf(0) }
    val density = LocalDensity.current
    var subcategoryButtonsHeight by remember { mutableStateOf(0.dp) }

    // Initialize ViewModel on first composition
    LaunchedEffect(Unit) {
        eventsViewModel.initialize()
    }

    // Update selected subcategory when ViewModel state changes
    LaunchedEffect(currentVisitsCategory, currentEventsCategory) {
        selectedSubcategory = when (currentMainTab) {
            MainTab.VISITS -> when (currentVisitsCategory) {
                VisitsCategory.WILLGO -> 0
                VisitsCategory.VISITED -> 1
            }

            MainTab.EVENTS -> when (currentEventsCategory) {
                EventsCategory.DRAFTS -> 0
                EventsCategory.PLANNED -> 1
                EventsCategory.COMPLETED -> 2
            }
        }
    }

    val gridState = rememberLazyGridState()
    val isScrolled by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 0 || gridState.firstVisibleItemScrollOffset > 0
        }
    }

    val subcategoryLabels = when (currentMainTab) {
        MainTab.VISITS -> listOf(
            VisitsCategory.WILLGO.displayName,
            VisitsCategory.VISITED.displayName
        )

        MainTab.EVENTS -> listOf(
            EventsCategory.DRAFTS.displayName,
            EventsCategory.PLANNED.displayName,
            EventsCategory.COMPLETED.displayName
        )
    }

    val topPadding by animateDpAsState(
        targetValue = if (isScrolled) 0.dp else subcategoryButtonsHeight + 12.dp
    )
    val cornerRadius by animateDpAsState(targetValue = if (isScrolled) 0.dp else 24.dp)

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
                                selected = currentMainTab == MainTab.VISITS,
                                onClick = {
                                    eventsViewModel.setMainTab(MainTab.VISITS)
                                })
                        }
                        item {
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                        item {
                            AnimatedSelectionChip(
                                text = "Мероприятия",
                                selected = currentMainTab == MainTab.EVENTS,
                                onClick = {
                                    eventsViewModel.setMainTab(MainTab.EVENTS)
                                }
                            )
                        }
                    }
                }
            )

            if (showFilter && filterState != null) {
                FilterDialog(
                    initialSortBy = filterState!!.sortBy,
                    initialSortOrder = filterState!!.sortOrder,
                    mainTab = currentMainTab,
                    visitsCategory = currentVisitsCategory,
                    eventsCategory = currentEventsCategory,
                    onDismiss = { showFilter = false },
                    onApply = { sortBy, sortOrder ->
                        eventsViewModel.updateSortOrder(sortBy, sortOrder)
                        showFilter = false
                    }
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                AnimatedContent(
                    targetState = subcategoryLabels
                ) { targetState ->
                    SubcategoryButtons(
                        labels = targetState,
                        selectedIndex = selectedSubcategory,
                        onSelect = { index ->
                            selectedSubcategory = index
                            when (currentMainTab) {
                                MainTab.VISITS -> {
                                    val category = when (index) {
                                        0 -> VisitsCategory.WILLGO
                                        1 -> VisitsCategory.VISITED
                                        else -> VisitsCategory.WILLGO
                                    }
                                    eventsViewModel.setVisitsCategory(category)
                                }

                                MainTab.EVENTS -> {
                                    val category = when (index) {
                                        0 -> EventsCategory.DRAFTS
                                        1 -> EventsCategory.PLANNED
                                        2 -> EventsCategory.COMPLETED
                                        else -> EventsCategory.DRAFTS
                                    }
                                    eventsViewModel.setEventsCategory(category)
                                }
                            }
                        },
                        modifier = Modifier.onGloballyPositioned { coordinates ->
                            subcategoryButtonsHeight =
                                with(density) { coordinates.size.height.toDp() }
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = topPadding, bottom = 12.dp)
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
                            TinyEventCard(
                                event = event,
                                onClick = { navController.navigate("event/${event.id}") }
                            )
                        }
                    }
                }
            }
        }



        AnimatedVisibility(
            visible = currentMainTab == MainTab.EVENTS && subcategoryLabels.getOrNull(
                selectedSubcategory
            ) == EventsCategory.DRAFTS.displayName,
            enter = scaleIn(),
            exit = scaleOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(horizontal = 24.dp, vertical = 24.dp),
        ) {
            FloatingActionButton(
                onClick = { navController.navigate("event-creation") },
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

