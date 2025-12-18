package ru.truebusiness.liveposter_android_client.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import ru.truebusiness.liveposter_android_client.R
import ru.truebusiness.liveposter_android_client.ui.theme.MainPageBodyColor
import ru.truebusiness.liveposter_android_client.ui.theme.MainPageTopFooterColor
import ru.truebusiness.liveposter_android_client.view.components.AppNavigationBar
import ru.truebusiness.liveposter_android_client.view.components.EventCard
import ru.truebusiness.liveposter_android_client.view.viewmodel.EventsViewModel
import java.util.Collections.emptyList

// TODO(e.vartazaryan): надо будет переделать через получение категорий с бекенда
// TODO(e.vartazaryan): если мероприятий нет, то отображать "Тут пока пусто..."

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    navController: NavController = rememberNavController(),
    eventsViewModel: EventsViewModel = viewModel()
) {
    val eventsState = eventsViewModel.events.observeAsState(emptyList())
    val events = eventsState.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF6600)
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
                                .width(55.dp)
                                .height(45.dp)
                                .padding(start = 10.dp)
                                .background(color = Color.White, shape = CircleShape)
                                .clickable(onClick = { navController.navigate("profile-settings") })
                        ) {
                            AsyncImage(
                                model = "https://i.pinimg.com/236x/c6/00/f2/c600f276b3f7cafcd572402ac86e489b.jpg",
                                contentDescription = "Avatar",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Text(
                            text = "Василий Попов",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            modifier = Modifier.padding(start = 18.dp)
                        )

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(end = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(
                                onClick = {
                                    navController.navigate("registration")
                                },
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(40.dp),
                                content = {
                                    Image(
                                        painter = painterResource(id = R.drawable.icon_bell),
                                        contentDescription = "Bell",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            AppNavigationBar(navController, "main")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MainPageBodyColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MainPageTopFooterColor)
            ) {}

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(events) { event ->
                    EventCard(
                        event = event,
                        onClick = { navController.navigate("event/${event.id}") }
                    )
                }

            }
        }
    }

    LaunchedEffect(Unit) {
        if (events.isEmpty()) {
            eventsViewModel.loadEvents()
        }
    }
}
