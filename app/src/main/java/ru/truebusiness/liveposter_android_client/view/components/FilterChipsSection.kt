package ru.truebusiness.liveposter_android_client.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.truebusiness.liveposter_android_client.view.organizationslist.OrganizationFilter

@Composable
fun FilterChipsSection(
    selected: OrganizationFilter,
    onSelected: (OrganizationFilter) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChipPill(
            text = "Все",
            selected = selected == OrganizationFilter.All,
            onClick = { onSelected(OrganizationFilter.All) }
        )
        FilterChipPill(
            text = "Подписки",
            selected = selected == OrganizationFilter.Subscriptions,
            onClick = { onSelected(OrganizationFilter.Subscriptions) }
        )
        FilterChipPill(
            text = "Мои Организации",
            selected = selected == OrganizationFilter.MyOrganizations,
            onClick = { onSelected(OrganizationFilter.MyOrganizations) }
        )
    }
}


@Composable
private fun FilterChipPill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (selected) Color(0xFF121212) else Color.White
    val fg = if (selected) Color.White else Color(0xFF121212)

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = bg,
        shadowElevation = 1.dp
    ) {
        Text(
            text = text,
            color = fg,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            maxLines = 1,
        )
    }
}