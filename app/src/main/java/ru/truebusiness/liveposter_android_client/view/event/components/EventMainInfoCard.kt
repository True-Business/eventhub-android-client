package ru.truebusiness.liveposter_android_client.view.event.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.truebusiness.liveposter_android_client.view.viewmodel.EventLocationUiState
import ru.truebusiness.liveposter_android_client.view.viewmodel.EventMainInfoUiState
import ru.truebusiness.liveposter_android_client.view.viewmodel.EventStatusType
import ru.truebusiness.liveposter_android_client.view.viewmodel.EventStatusUiState

@Composable
internal fun EventMainInfoCard(state: EventMainInfoUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            state.posterUrl?.let { posterUrl ->
                AsyncImage(
                    model = posterUrl,
                    contentDescription = state.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = state.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                state.scheduleText?.let { schedule ->
                    InfoRow(
                        icon = Icons.Filled.DateRange,
                        contentDescription = "Дата и время",
                        text = schedule
                    )
                }
                EventLocationSection(state.location)
            }

            HorizontalDivider(Modifier, DividerDefaults.Thickness, color = Color(0xFFEAEAEA))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EventStatusChip(state.status)

                state.participantsText?.let { participants ->
                    Text(
                        text = "Участники: $participants",
                        color = Color(0xFF5F6368),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun EventLocationSection(locationState: EventLocationUiState) {
    when {
        locationState.city != null -> {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF5F6368)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = locationState.city, color = Color(0xFF5F6368))
                    val address = locationState.address ?: locationState.location
                    if (address != null) {
                        Text(
                            text = address,
                            color = Color(0xFF8A8A8A),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        locationState.location != null -> {
            InfoRow(
                icon = Icons.Filled.LocationOn,
                contentDescription = "Место проведения",
                text = locationState.location
            )
        }
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String?,
    text: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color(0xFF5F6368)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = Color(0xFF5F6368))
    }
}

@Composable
private fun EventStatusChip(status: EventStatusUiState) {
    val (background, textColor) = when (status.type) {
        EventStatusType.CLOSED -> Color(0xFFFFE5E5) to Color(0xFFD32F2F)
        EventStatusType.OPEN -> Color(0xFFE5F6EA) to Color(0xFF2E7D32)
    }

    AssistChip(
        onClick = {},
        label = {
            Text(text = status.label, color = textColor, fontSize = 12.sp)
        },
        enabled = false,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = background,
            disabledContainerColor = background,
            disabledLabelColor = textColor
        )
    )
}