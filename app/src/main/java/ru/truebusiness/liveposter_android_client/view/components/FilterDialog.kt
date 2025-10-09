package ru.truebusiness.liveposter_android_client.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


enum class MainTab { VISITS, EVENTS }


enum class VisitsCategory { GOING, VISITED, FAVORITES }


enum class EventsCategory { DRAFTS, PAST, PLANNED }


data class Item(
    val id: Int,
    val title: String,
    val subtitle: String
)


data class FilterState(
    val query: String = "",
    val onlyMine: Boolean = false,
    val sortBy: SortBy = SortBy.DATE_DESC
)


enum class SortBy { DATE_DESC, DATE_ASC, NAME }

@Composable
fun FilterDialog(
    initial: FilterState,
    onDismiss: () -> Unit,
    onApply: (FilterState) -> Unit
) {
    var query by remember { mutableStateOf(initial.query) }
    var onlyMine by remember { mutableStateOf(initial.onlyMine) }
    var sortBy by remember { mutableStateOf(initial.sortBy) }


    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Фильтры", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(12.dp))


                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Поиск") },
                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(Modifier.height(8.dp))


                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = onlyMine, onCheckedChange = { onlyMine = it })
                    Spacer(Modifier.width(8.dp))
                    Text(text = "Только мои")
                }


                Spacer(Modifier.height(12.dp))


                Text(text = "Сортировка")
                Column {
                    RadioButtonWithText(
                        selected = sortBy == SortBy.DATE_DESC,
                        onSelect = { sortBy = SortBy.DATE_DESC },
                        text = "По убыванию даты"
                    )
                    RadioButtonWithText(
                        selected = sortBy == SortBy.DATE_ASC,
                        onSelect = { sortBy = SortBy.DATE_ASC },
                        text = "По возрастанию даты"
                    )
                    RadioButtonWithText(
                        selected = sortBy == SortBy.NAME,
                        onSelect = { sortBy = SortBy.NAME },
                        text = "По названию"
                    )
                }


                Spacer(Modifier.height(16.dp))


                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) { Text("Назад") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        onApply(
                            FilterState(
                                query,
                                onlyMine,
                                sortBy
                            )
                        )
                    }) { Text("Применить") }
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
        RadioButton(selected = selected, onClick = onSelect)
        Spacer(Modifier.width(8.dp))
        Text(text = text)
    }
}