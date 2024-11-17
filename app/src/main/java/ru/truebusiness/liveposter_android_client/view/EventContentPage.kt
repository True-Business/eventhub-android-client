package ru.truebusiness.liveposter_android_client.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.ui.theme.EventPageBodyColor
import ru.truebusiness.liveposter_android_client.ui.theme.EventPageTopFooterColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsPage(event: Event, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(EventPageBodyColor)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = event.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            modifier = Modifier.background(color = EventPageTopFooterColor),
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Дата начала: ${event.startDate}")
        Text(text = "Дата окончания: ${event.endDate}")
        Text(text = "Место: ${event.location}")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = event.content)
        Spacer(modifier = Modifier.height(24.dp))
    }
}