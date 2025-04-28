package ru.truebusiness.liveposter_android_client.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.truebusiness.liveposter_android_client.ui.theme.UserInterestRegistrationPageCategoryChipColor
import ru.truebusiness.liveposter_android_client.ui.theme.UserInterestRegistrationPageCitySelectionMenuColor
import ru.truebusiness.liveposter_android_client.ui.theme.UserInterestRegistrationPageContinueButtonColor
import ru.truebusiness.liveposter_android_client.ui.theme.UserInterestRegistrationPageRadioButtonSelectedColor

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RegistrationPageUserInterest(navController: NavController) {
    var selectedCategories = remember { mutableStateListOf<String>() }
    var selectedOpenness by remember { mutableStateOf("") }
    var selectedPaymentType by remember { mutableStateOf("") }
    var selectedPriceRange by remember { mutableStateOf("") }
    var selectedDayTime by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("") }
    var organizerRatingImportance by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = "Настройка интересов",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        QuestionSection(
            title = "1. Какие типы мероприятий Вам интересны?",
            content = {
                //TODO: сделать получение категорий из БД
                val categories = listOf("Концерты", "Выставки", "Мастер-классы", "Спорт", "Кино")

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        FilterChip(
                            selected = selectedCategories.contains(category),
                            onClick = {
                                if (selectedCategories.contains(category)) {
                                    selectedCategories.remove(category)
                                } else {
                                    selectedCategories.add(category)
                                }
                            },
                            label = { Text(category) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = UserInterestRegistrationPageCategoryChipColor
                            )
                        )
                    }
                }
            }
        )

        // 2. Предпочтения по открытости мероприятий
        QuestionSection(
            title = "2. Вы предпочитаете:",
            content = {
                val options = listOf(
                    "Публичные",
                    "Закрытые",
                    "По приглашению",
                    "Не важно"
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    options.forEach { option ->
                        RadioButtonItem(
                            text = option,
                            selected = selectedOpenness == option,
                            onSelect = { selectedOpenness = option }
                        )
                    }
                }
            }
        )

        // 3. Предпочтения по оплате
        QuestionSection(
            title = "3. Вы предпочитаете:",
            content = {
                val options = listOf(
                    "Бесплатные",
                    "Платные",
                    "Не важно"
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    options.forEach { option ->
                        RadioButtonItem(
                            text = option,
                            selected = selectedPaymentType == option,
                            onSelect = { selectedPaymentType = option }
                        )
                    }
                }
            }
        )

        // 4. Ценовая категория
        QuestionSection(
            title = "4. Какая ценовая категория для Вас предпочтительна?",
            content = {
                val priceRanges = listOf(
                    "До 500 рублей",
                    "500-1000 рублей",
                    "1000-2000 рублей",
                    "Более 2000 рублей",
                    "Не важно"
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    priceRanges.forEach { range ->
                        RadioButtonItem(
                            text = range,
                            selected = selectedPriceRange == range,
                            onSelect = { selectedPriceRange = range }
                        )
                    }
                }
            }
        )

        // 5. Время суток
        QuestionSection(
            title = "5. В какое время суток Вы обычно ходите на мероприятия?",
            content = {
                val dayTimes = listOf(
                    "Утро (9:00-12:00)",
                    "День (12:00-18:00)",
                    "Вечер (18:00-23:00)",
                    "Ночь (после 23:00)",
                    "Не важно"
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    dayTimes.forEach { time ->
                        RadioButtonItem(
                            text = time,
                            selected = selectedDayTime == time,
                            onSelect = { selectedDayTime = time }
                        )
                    }
                }
            }
        )

        // 6. Город
        QuestionSection(
            title = "6. В каком городе Вы ищите мероприятия?",
            content = {
                // Здесь можно реализовать поиск с подсказками
                var expanded by remember { mutableStateOf(false) }
                val cities = listOf("Москва", "Санкт-Петербург", "Новосибирск", "Екатеринбург")

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCity,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().border(1.dp, color = Color.Black)
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        cities.forEach { city ->
                            DropdownMenuItem(
                                text = { Text(city) },
                                onClick = {
                                    selectedCity = city
                                    expanded = false
                                },
                                modifier = Modifier.background(Color.White)
                            )
                        }
                    }
                }
            }
        )

        // 7. Важность мнения других
        QuestionSection(
            title = "7. Важно ли для Вас мнение других людей о прошедших мероприятиях организатора и рейтинг организатора?",
            content = {
                val options = listOf(
                    "Для меня важно мнение других людей и рейтинг организатора",
                    "Интересуюсь мнением людей, но предпочитаю складывать своё мнение",
                    "Всё равно на мнение других, предпочитаю складывать своё мнение"
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    options.forEach { option ->
                        RadioButtonItem(
                            text = option,
                            selected = organizerRatingImportance == option,
                            onSelect = { organizerRatingImportance = option }
                        )
                    }
                }
            }
        )

        // Кнопка завершения
        Button(
            onClick = {
                if (selectedCategories.isEmpty() || selectedCity.isEmpty()) {
                    Toast.makeText(context, "Пожалуйста, ответьте на все обязательные вопросы", Toast.LENGTH_SHORT).show()
                } else {
                    val userPreferences = UserPreferences(
                        categories = selectedCategories,
                        openness = selectedOpenness,
                        paymentType = selectedPaymentType,
                        priceRange = selectedPriceRange,
                        dayTime = selectedDayTime,
                        city = selectedCity,
                        ratingImportance = organizerRatingImportance
                    )

                    navController.navigate("main") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = UserInterestRegistrationPageContinueButtonColor
            )
        ) {
            Text(text = "Завершить регистрацию", color = Color.Black)
        }
    }
}

@Composable
private fun QuestionSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
        )
        content()
    }
}

@Composable
private fun RadioButtonItem(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 4.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            modifier = Modifier.size(24.dp),
            colors = RadioButtonDefaults.colors(
                selectedColor = UserInterestRegistrationPageRadioButtonSelectedColor
            )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

// Модель для хранения предпочтений
data class UserPreferences(
    val categories: List<String>,
    val openness: String,
    val paymentType: String,
    val priceRange: String,
    val dayTime: String,
    val city: String,
    val ratingImportance: String
)