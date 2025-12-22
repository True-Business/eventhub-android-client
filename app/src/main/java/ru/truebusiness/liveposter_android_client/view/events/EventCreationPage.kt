package ru.truebusiness.liveposter_android_client.view.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.truebusiness.liveposter_android_client.R
import ru.truebusiness.liveposter_android_client.view.viewmodel.EventCreationViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


private val orange = Color(0xFFFF6600)

@Composable
fun EventCreationPage(
    navController: NavController,
    viewModel: EventCreationViewModel
) {
    val isFirstPage = viewModel.isFirstPage
    if (isFirstPage)
        EventCreationFirstPage(navController, viewModel)
    else
        EventCreationSettingsPage(navController, viewModel)
}

@Composable
private fun EventCreationFirstPage(
    navController: NavController,
    viewModel: EventCreationViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.size(24.dp))
        Image(
            painter = painterResource(id = R.drawable.first_step_event_creation),
            contentDescription = "First step",
            modifier = Modifier.height(48.dp)
        )

        Spacer(Modifier.size(24.dp))

        Text(
            text = "Создание мероприятия",
            color = orange,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.size(16.dp))

        InputForm(viewModel)

        Spacer(Modifier.size(24.dp))

        BackButton(navController)
        Spacer(Modifier.size(8.dp))
        SaveDraftButton(navController, viewModel::isInfoValid, viewModel::onDraftSave)
        Spacer(Modifier.size(8.dp))
        ContinueButton(viewModel::onNextPage)
    }
}

@Composable
private fun ContinueButton(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(orange)
            .padding(16.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = orange,
        contentColor = Color.White
    ) {
        Text(
            text = "Продолжить",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SaveDraftButton(
    navController: NavController,
    isValid: () -> Boolean,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, orange, RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable(onClick = {
                if (isValid()) {
                    onClick()
                    navController.popBackStack()
                } else {
                    android.widget.Toast
                        .makeText(
                            context,
                            "Не все обязательные поля заполнены корректно, проверьте данные",
                            android.widget.Toast.LENGTH_SHORT
                        )
                        .show()
                }
            }),
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        contentColor = orange
    ) {
        Text(
            text = "Сохранить черновик",
            color = orange,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BackButton(navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable(onClick = { navController.popBackStack() }),
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        contentColor = Color.Black
    ) {
        Text(
            text = "Отмена",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun InputForm(viewModel: EventCreationViewModel) {
    val state = viewModel.infoState
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = orange,
                spotColor = orange
            ),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 32.dp, horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            InputTextField(
                title = "Название мероприятия",
                placeholder = "Введите название",
                value = state.title,
                comment = "От 3 до 128 символов",
                isObligatory = true,
                onChange = viewModel::updateTitle
            )
            InputTextField(
                title = "Описание мероприятия",
                placeholder = "Опишите мероприятие",
                value = state.description,
                comment = "От 3 до 1024 символов",
                isObligatory = true,
                onChange = viewModel::updateDescription
            )
            InputTextField(
                title = "Город",
                placeholder = "Укажите город",
                value = state.city,
                isObligatory = true,
                onChange = viewModel::updateCity
            )
            InputTextField(
                title = "Адрес",
                placeholder = "Улица, дом, корпус",
                value = state.address,
                isObligatory = true,
                onChange = viewModel::updateAddress
            )
            InputTextField(
                title = "Как добраться",
                placeholder = "Инструкции для участников",
                value = state.howToGet,
                isObligatory = true,
                onChange = viewModel::updateHowToGet
            )
            DateInputField(
                title = "Дата начала",
                isObligatory = true,
                value = state.startDate,
                onDateSelected = viewModel::updateStartDate
            )
            TimeInputField(
                title = "Время начала",
                isObligatory = true,
                value = state.startTime,
                onTimeSelected = viewModel::updateStartTime
            )
            DateInputField(
                title = "Дата окончания",
                isObligatory = true,
                value = state.endDate,
                onDateSelected = viewModel::updateEndDate
            )
            TimeInputField(
                title = "Время окончания",
                isObligatory = false,
                value = state.endTime,
                onTimeSelected = viewModel::updateEndTime
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputTextField(
    title: String,
    placeholder: String,
    value: String,
    comment: String? = null,
    isObligatory: Boolean,
    onChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row {
            Text(
                text = title,
                fontSize = 16.sp
            )
            if (isObligatory) {
                Text(
                    text = " *",
                    color = orange,
                    fontSize = 16.sp
                )
            }
        }
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = false,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = orange,
                unfocusedBorderColor = Color.Gray,
                cursorColor = orange
            )
        )
        comment?.let { comment ->
            Text(
                text = comment,
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                color = Color.Gray.copy(alpha = 0.7f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInputField(
    title: String,
    isObligatory: Boolean,
    value: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val formatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy") }
    var text by remember { mutableStateOf(value?.format(formatter) ?: "") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            Text(text = title, fontSize = 16.sp)
            if (isObligatory) Text(" *", color = orange, fontSize = 16.sp)
        }

        OutlinedTextField(
            value = text,
            onValueChange = { newValue ->
                if (newValue.length <= 10 && newValue.all { it.isDigit() || it == '.' }) {
                    text = newValue
                    runCatching {
                        val parsed = LocalDate.parse(newValue, formatter)
                        onDateSelected(parsed)
                    }
                }
            },
            trailingIcon = {
                androidx.compose.material3.Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = "Выбрать дату",
                    modifier = Modifier.clickable {
                        val now = value ?: LocalDate.now()
                        DatePickerDialog(
                            context,
                            { _, y, m, d ->
                                val selected = LocalDate.of(y, m + 1, d)
                                text = selected.format(formatter)
                                onDateSelected(selected)
                            },
                            now.year,
                            now.monthValue - 1,
                            now.dayOfMonth
                        ).show()
                    },
                    tint = orange
                )
            },
            placeholder = { Text("дд.мм.гггг") },
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = orange,
                unfocusedBorderColor = Color.Gray,
                cursorColor = orange
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeInputField(
    title: String,
    isObligatory: Boolean,
    value: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit
) {
    val context = LocalContext.current
    val formatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
    var text by remember { mutableStateOf(value?.format(formatter) ?: "") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            Text(text = title, fontSize = 16.sp)
            if (isObligatory) Text(" *", color = orange, fontSize = 16.sp)
        }

        OutlinedTextField(
            value = text,
            onValueChange = { newValue ->
                if (newValue.length <= 5 && newValue.all { it.isDigit() || it == ':' }) {
                    text = newValue
                    runCatching {
                        val parsed = LocalTime.parse(newValue, formatter)
                        onTimeSelected(parsed)
                    }
                }
            },
            trailingIcon = {
                androidx.compose.material3.Icon(
                    painter = painterResource(id = R.drawable.ic_clock),
                    contentDescription = "Выбрать время",
                    modifier = Modifier.clickable {
                        val now = value ?: LocalTime.now()
                        TimePickerDialog(
                            context,
                            { _, hour, minute ->
                                val selected = LocalTime.of(hour, minute)
                                text = selected.format(formatter)
                                onTimeSelected(selected)
                            },
                            now.hour,
                            now.minute,
                            true
                        ).show()
                    },
                    tint = orange
                )
            },
            placeholder = { Text("чч:мм") },
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = orange,
                unfocusedBorderColor = Color.Gray,
                cursorColor = orange
            )
        )
    }
}

@Composable
private fun showValidationErrorToast() {
    val context = LocalContext.current
    android.widget.Toast
        .makeText(
            context,
            "Не все обязательные поля заполнены корректно, проверьте данные",
            android.widget.Toast.LENGTH_SHORT
        )
        .show()
}
