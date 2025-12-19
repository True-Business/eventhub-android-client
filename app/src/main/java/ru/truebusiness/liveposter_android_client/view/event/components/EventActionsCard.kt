package ru.truebusiness.liveposter_android_client.view.event.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ru.truebusiness.liveposter_android_client.view.components.GradientButton
import ru.truebusiness.liveposter_android_client.view.viewmodel.EventActionsUiState
import kotlin.math.max

@Composable
fun EventActionsCard(
    state: EventActionsUiState,
    onPrimaryAction: () -> Unit,
    onShareClick: () -> Unit,
    onShowParticipants: () -> Unit,
    onEditClick: () -> Unit,
    onCancelEventClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            state.primaryButton?.let { buttonState ->
                GradientButton(
                    text = buttonState.text,
                    enabled = buttonState.enabled,
                    onClick = onPrimaryAction
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EventOutlinedButton(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.Share,
                    label = "Поделиться",
                    onClick = onShareClick
                )

                EventOutlinedButton(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.Person,
                    label = "Участники",
                    onClick = onShowParticipants
                )
            }

            EventOutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Filled.Edit,
                label = "Редактировать",
                onClick = onEditClick
            )

            EventOutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Outlined.Delete,
                label = "Отменить",
                onClick = onCancelEventClick
            )
        }
    }
}

@Composable
private fun EventOutlinedButton(
    modifier: Modifier,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(Modifier.size(8.dp))
            Text(text = label, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
        }

    }
}