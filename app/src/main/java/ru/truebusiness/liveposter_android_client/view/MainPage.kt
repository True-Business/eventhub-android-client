package ru.truebusiness.liveposter_android_client.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.truebusiness.liveposter_android_client.R
import ru.truebusiness.liveposter_android_client.data.EventCategory
import ru.truebusiness.liveposter_android_client.ui.theme.MainPageBodyColor
import ru.truebusiness.liveposter_android_client.ui.theme.MainPageEventButtonColor
import ru.truebusiness.liveposter_android_client.ui.theme.MainPageTopFooterButtonColor
import ru.truebusiness.liveposter_android_client.ui.theme.MainPageTopFooterButtonPressedColor
import ru.truebusiness.liveposter_android_client.ui.theme.MainPageTopFooterColor
import ru.truebusiness.liveposter_android_client.ui.theme.MainPageUserUnfoFooterColor
import ru.truebusiness.liveposter_android_client.view.viewmodel.EventsViewModel

// TODO(e.vartazaryan): надо будет переделать через получение категорий с бекенда
// TODO(e.vartazaryan): если мероприятий нет, то отображать "Тут пока пусто..."

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MainPage(navController: NavController = rememberNavController(), eventsViewModel: EventsViewModel = viewModel()) {

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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp)
    ) {
        TopAppBar(
            title = {},
            modifier = Modifier.fillMaxWidth(),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MainPageUserUnfoFooterColor
            ),
            actions = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .width(200.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .width(65.dp) // Не знаю почему при 40 его сжимает по горизонтали
                            .height(55.dp)
                            .padding(start = 10.dp)
                            .background(color = Color.White, shape = CircleShape)
                    ) {
                        Text(
                            text = "ВЭ",
                            modifier = Modifier
                                .padding(start = 18.dp, top = 18.dp)
                        )
                    }

                    Text(
                        text = "Вартазарян Эдуард",
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 30.dp)
                    )

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp),
                            content = {
                                Image(
                                    painter = painterResource(id = R.drawable.bell),
                                    contentDescription = "Bell",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        )
                    }
                }
            }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MainPageTopFooterColor)
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 8.dp),
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .background(MainPageTopFooterColor)
            ) {
                items(EventCategory.values()) { category ->

                    Button(
                        onClick = {
                            eventsViewModel.setCategory(category)
                        },
                        modifier = Modifier
                            .padding(4.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedCategory == category)
                                MainPageTopFooterButtonPressedColor
                            else
                                MainPageTopFooterButtonColor
                        )
                    ) {
                        Text(category.displayName)
                    }
                }
            }
        }

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