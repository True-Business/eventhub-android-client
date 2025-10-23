package ru.truebusiness.liveposter_android_client.view.components

import android.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.truebusiness.liveposter_android_client.data.SortField
import ru.truebusiness.liveposter_android_client.data.SortOrder
import ru.truebusiness.liveposter_android_client.data.MainTab
import ru.truebusiness.liveposter_android_client.data.VisitsCategory
import ru.truebusiness.liveposter_android_client.data.EventsCategory
import ru.truebusiness.liveposter_android_client.data.getAvailableSortFields
import ru.truebusiness.liveposter_android_client.ui.theme.accentColor
import ru.truebusiness.liveposter_android_client.ui.theme.accentColorText

@Composable
fun FilterDialog(
    initialSortBy: SortField,
    initialSortOrder: SortOrder,
    mainTab: MainTab,
    visitsCategory: VisitsCategory? = null,
    eventsCategory: EventsCategory? = null,
    onDismiss: () -> Unit,
    onApply: (SortField, SortOrder) -> Unit
) {
    var sortBy by remember { mutableStateOf(initialSortBy) }
    var sortOrder by remember { mutableStateOf(initialSortOrder) }
    
    // Get available sort fields for current tab/category
    val availableSortFields = remember(mainTab, visitsCategory, eventsCategory) {
        getAvailableSortFields(mainTab, visitsCategory, eventsCategory)
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Сортировка", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(12.dp))

                Text(text = "Поле для сортировки")
                Column {
                    availableSortFields.forEach { field ->
                        RadioButtonWithText(
                            selected = sortBy == field,
                            onSelect = { sortBy = field },
                            text = when (field) {
                                SortField.START_DATE -> "Дата начала"
                                SortField.TITLE -> "Название"
                                SortField.PRICE -> "Цена"
                                SortField.LOCATION -> "Местоположение"
                            }
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(text = "Порядок сортировки")
                Column {
                    RadioButtonWithText(
                        selected = sortOrder == SortOrder.ASC,
                        onSelect = { sortOrder = SortOrder.ASC },
                        text = "По возрастанию"
                    )
                    RadioButtonWithText(
                        selected = sortOrder == SortOrder.DESC,
                        onSelect = { sortOrder = SortOrder.DESC },
                        text = "По убыванию"
                    )
                }

                Spacer(Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) { Text("Назад", color = accentColorText) }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { onApply(sortBy, sortOrder) },
                        colors = ButtonDefaults.buttonColors(accentColor)
                    ) { Text("Применить") }
                }
            }
        }
    }
}


@Composable
private fun RadioButtonWithText(selected: Boolean, onSelect: () -> Unit, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(accentColor)
        )
        Spacer(Modifier.width(8.dp))
        Text(text = text)
    }
}