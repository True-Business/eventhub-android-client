package ru.truebusiness.liveposter_android_client.view.organizationslist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.truebusiness.liveposter_android_client.view.organizationslist.OrganizationTab

@Composable
fun FilterTabsSection(
    selected: OrganizationTab,
    onSelected: (OrganizationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChipPill(
            text = "Все",
            selected = selected == OrganizationTab.ALL,
            onClick = { onSelected(OrganizationTab.ALL) }
        )
        FilterChipPill(
            text = "Подписки",
            selected = selected == OrganizationTab.SUBSCRIPTIONS,
            onClick = { onSelected(OrganizationTab.SUBSCRIPTIONS) }
        )
        FilterChipPill(
            text = "Мои Организации",
            selected = selected == OrganizationTab.MINE,
            onClick = { onSelected(OrganizationTab.MINE) }
        )
    }
}

@Composable
private fun FilterChipPill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (selected) Color.Black else Color.White
    val fg = if (selected) Color.White else Color.Black

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = bg,
        shadowElevation = 2.dp
    ) {
        Text(
            text = text,
            color = fg,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            maxLines = 1
        )
    }
}