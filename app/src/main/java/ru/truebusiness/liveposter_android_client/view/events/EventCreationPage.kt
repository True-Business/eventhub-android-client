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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.truebusiness.liveposter_android_client.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


private val orange = Color(0xFFFF6600)

@Composable
fun EventCreationPage(
    navController: NavController?
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

        InputForm()

        Spacer(Modifier.size(24.dp))

        BackButton(navController)
        Spacer(Modifier.size(8.dp))
        SaveDraftButton()
        Spacer(Modifier.size(8.dp))
        ContinueButton(navController)
    }
}

@Composable
private fun ContinueButton(navController: NavController?) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(orange)
            .padding(16.dp)
            .clickable(onClick = { navController!!.navigate("event-creation-settings") }),
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
private fun SaveDraftButton() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, orange, RoundedCornerShape(8.dp))
            .padding(16.dp),
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
private fun BackButton(navController: NavController?) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(16.dp),
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
private fun InputForm() {
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
                comment = "От 3 до 128 символов",
                isObligatory = true,
                onChange = { }
            )
            InputTextField(
                title = "Описание мероприятия",
                placeholder = "Опишите мероприятие",
                comment = "От 3 до 1024 символов",
                isObligatory = true,
                onChange = { }
            )
            InputTextField(
                title = "Город",
                placeholder = "Укажите город",
                isObligatory = true,
                onChange = { }
            )
            InputTextField(
                title = "Адрес",
                placeholder = "Улица, дом, корпус",
                isObligatory = true,
                onChange = { }
            )
            InputTextField(
                title = "Как добраться",
                placeholder = "Инструкции для участников",
                isObligatory = true,
                onChange = { }
            )
            DateInputField(
                title = "Дата начала",
                isObligatory = true,
                onDateSelected = { }
            )
            TimeInputField(
                title = "Время начала",
                isObligatory = true,
                onTimeSelected = { }
            )
            TimeInputField(
                title = "Время окончания",
                isObligatory = false,
                onTimeSelected = { }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputTextField(
    title: String,
    placeholder: String,
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
            value = "",
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
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormatter = remember { SimpleDateFormat("dd.mm.yyyy", Locale.getDefault()) }
    var dateText by remember { mutableStateOf("dd.mm.yyyy") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
            value = dateText,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            calendar.set(year, month, dayOfMonth)
                            val formattedDate = dateFormatter.format(calendar.time)
                            dateText = formattedDate
                            onDateSelected(formattedDate)
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
            shape = RoundedCornerShape(16.dp),
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
    onTimeSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val timeFormatter = remember { SimpleDateFormat("hh:mm", Locale.getDefault()) }
    var timeText by remember { mutableStateOf("hh:mm") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
            value = timeText,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    TimePickerDialog(
                        context,
                        { _, hourOfDay, minute ->
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendar.set(Calendar.MINUTE, minute)
                            val formattedTime = timeFormatter.format(calendar.time)
                            timeText = formattedTime
                            onTimeSelected(formattedTime)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()
                },
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = orange,
                unfocusedBorderColor = Color.Gray,
                cursorColor = orange
            )
        )
    }
}


@Preview
@Composable
private fun Preview() {
    EventCreationPage(null)
}