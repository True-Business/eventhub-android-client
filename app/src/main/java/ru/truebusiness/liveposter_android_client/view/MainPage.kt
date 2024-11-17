package ru.truebusiness.liveposter_android_client.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.truebusiness.liveposter_android_client.data.EventCategory
import ru.truebusiness.liveposter_android_client.ui.theme.MainPageBodyColor
import ru.truebusiness.liveposter_android_client.ui.theme.MainPageEventButtonColor
import ru.truebusiness.liveposter_android_client.ui.theme.TopFooterButtonColor
import ru.truebusiness.liveposter_android_client.ui.theme.TopFooterButtonPressedColor
import ru.truebusiness.liveposter_android_client.ui.theme.TopFooterColor
import ru.truebusiness.liveposter_android_client.view.viewmodel.EventsViewModel

// TODO(e.vartazaryan): надо будет переделать через получение категорий с бекенда
// TODO(e.vartazaryan): надо заменить список из "Привет" на загрузку мероприятий с бекенда

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(navController: NavController, eventsViewModel: EventsViewModel = viewModel()) {

    val tag = "MainPage"

    val eventsState = eventsViewModel.events.observeAsState(emptyList())
    val events = eventsState.value

    val isLoading = eventsViewModel.isLoading.observeAsState(false)

    val selectedCategoryState = eventsViewModel.selectedCategory.observeAsState(EventCategory.ALL)
    val selectedCategory = selectedCategoryState.value

    // эта штука будет отвечать за состояние списка событий
    val listState = rememberLazyListState()
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == events.size - 1 }
            .collect { isEndOfList ->
                if (isEndOfList && !isLoading.value) {
                    Log.v(tag, ": loading new events...")
                    eventsViewModel.loadEvents()
                }
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    text = "Категории",
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            actions = {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    modifier = Modifier
                        .background(TopFooterColor)
                ) {
                    items(EventCategory.values()) { category ->

                        Button(onClick = {
                            eventsViewModel.setCategory(category)
                        }, modifier = Modifier
                            .padding(4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedCategoryState.value == category)
                                    TopFooterButtonPressedColor
                                else
                                    TopFooterButtonColor
                            )
                        ) {
                            Text(category.displayName)
                        }
                    }
                }
            }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MainPageBodyColor),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(events) { event ->
                    Button(
                        onClick = { navController.navigate("event/${event.id}") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MainPageEventButtonColor,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            event.title,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                item {
                    if (isLoading.value) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        if(events.isEmpty()) {
            eventsViewModel.loadEvents()
        }
    }
}