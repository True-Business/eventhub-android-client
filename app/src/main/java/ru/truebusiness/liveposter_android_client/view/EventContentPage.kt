package ru.truebusiness.liveposter_android_client.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.truebusiness.liveposter_android_client.data.Event

@Composable
fun EventDetailsPage(event: Event, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(text = event.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Дата начала: ${event.startDate}")
        Text(text = "Дата окончания: ${event.endDate}")
        Text(text = "Место: ${event.location}")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = event.content)

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBack) {
            Text("Назад")
        }
    }
}