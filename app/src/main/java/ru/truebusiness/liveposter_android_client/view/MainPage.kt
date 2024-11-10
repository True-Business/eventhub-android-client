package ru.truebusiness.liveposter_android_client.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.truebusiness.liveposter_android_client.view.viewmodel.EventsViewModel

// TODO(e.vartazaryan): надо будет переделать через получение категорий с бекенда
// TODO(e.vartazaryan): надо заменить список из "Привет" на загрузку мероприятий с бекенда

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(eventsViewModel: EventsViewModel = viewModel()) {

    val eventsState = eventsViewModel.events.observeAsState(emptyList())
    val events = eventsState.value
    val isLoading = eventsViewModel.isLoading.observeAsState(false)

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
                Button(onClick = {}) {
                    Text("Кино")
                }
                Button(onClick = {}) {
                    Text("Музыка")
                }
                Button(onClick = {}) {
                    Text("Рестораны")
                }
            }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(events) { event ->
                    Text(
                        event.title,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(8.dp)
                    )
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
        eventsViewModel.loadEvents()
    }
}