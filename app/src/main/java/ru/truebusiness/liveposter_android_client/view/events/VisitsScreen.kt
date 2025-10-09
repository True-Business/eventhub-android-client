package ru.truebusiness.liveposter_android_client.view.events

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.view.components.SubcategoryButtons
import ru.truebusiness.liveposter_android_client.view.components.TinyEventCard

@Composable
fun VisitsScreen(
    modifier: Modifier = Modifier,
    itemsProvider: (Int) -> List<Event>
) {
    var selectedCategory by remember { mutableStateOf(0) }
    val labels = listOf("Я пойду", "Посещенные", "Избранное")
    val events = itemsProvider(selectedCategory)
    val gridState = rememberLazyGridState()
    val isScrolled by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 0 || gridState.firstVisibleItemScrollOffset > 0
        }
    }
    val topPadding by animateDpAsState(targetValue = if (isScrolled) 0.dp else 175.dp)
    val cornerRadius by animateDpAsState(targetValue = if (isScrolled) 0.dp else 24.dp)

    Box(modifier = modifier.fillMaxSize()) {
        Column {
            SubcategoryButtons(
                labels = labels,
                selectedIndex = selectedCategory,
                onSelect = { selectedCategory = it }
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
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
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