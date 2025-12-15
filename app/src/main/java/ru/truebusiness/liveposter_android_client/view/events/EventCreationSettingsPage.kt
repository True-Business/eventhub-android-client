package ru.truebusiness.liveposter_android_client.view.events

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.truebusiness.liveposter_android_client.R
import ru.truebusiness.liveposter_android_client.view.viewmodel.EventCreationViewModel

private val orange = Color(0xFFFF6600)

@Composable
fun EventCreationSettingsPage(
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
            painter = painterResource(id = R.drawable.second_step_event_creation),
            contentDescription = "First step",
            modifier = Modifier.height(48.dp)
        )

        Spacer(Modifier.size(24.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.back_buttom),
                contentDescription = "back_button",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterStart)
                    .clickable(onClick = { navController.popBackStack() })
            )
            Text(
                text = "Настройки мероприятия",
                color = orange,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.size(24.dp))

        MainForm(viewModel)

        Spacer(Modifier.size(24.dp))

        Buttons(viewModel, navController)
    }
}

@Composable
private fun Buttons(
    viewModel: EventCreationViewModel,
    navController: NavController
) {
    BackButton(viewModel::onPrevPage)
    Spacer(Modifier.size(8.dp))
    SaveDraftButton(viewModel::onDraftSave, viewModel::isInfoValid, navController)
    Spacer(Modifier.size(8.dp))
    PublishButton(viewModel::onPublication, viewModel::isInfoValid, navController)
}

@Composable
private fun PublishButton(
    onClick: () -> Unit,
    isValid: () -> Boolean,
    navController: NavController
) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(orange)
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
        color = orange,
        contentColor = Color.White
    ) {
        Text(
            text = "Опубликовать",
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
    onClick: () -> Unit,
    isValid: () -> Boolean,
    navController: NavController
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
private fun BackButton(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable(onClick = { onClick() }),
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
private fun MainForm(vm: EventCreationViewModel) {
    val state = vm.settingsState

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
            verticalArrangement = Arrangement.spacedBy(24.dp)

        ) {
            EventTypeToggle(
                isClosed = state.isClosed,
                onToggle = { vm.setIsClosed(!state.isClosed) })

            AnimatedContent(
                targetState = state.isClosed,
                transitionSpec = {
                    fadeIn() + slideInVertically { fullHeight -> -fullHeight } togetherWith
                            fadeOut() + slideOutVertically { fullHeight -> -fullHeight }
                },
                contentAlignment = Alignment.Center
            ) { isClosed ->
                if (isClosed) ClosedEventView(vm)
                else OpenEventView(vm)
            }
        }
    }
}

@Composable
private fun OpenEventView(vm: EventCreationViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        InputTextField(
            title = "Ограничения для посетителей",
            placeholder = "Возрастные ограничения, дресс-код и т.д.",
            value = vm.settingsState.limits ?: "",
            isObligatory = false,
            onChange = vm::updateLimits
        )
        InputIntField(
            title = "Количество участников",
            placeholder = "Максимальное количество участников",
            value = vm.settingsState.participantsLimit,
            isObligatory = false,
            onChange = vm::updateParticipantsLimits
        )
        RegistrationRequirement(
            vm.settingsState.requiresRegistration,
            vm.settingsState.registrationFields,
            vm::setRequiresRegistration,
            vm::toggleRegistrationField
        )
    }
}

@Composable
fun RegistrationRequirement(
    requiresRegistration: Boolean,
    registrationFields: Map<String, Boolean>,
    onRequirementChange: (Boolean) -> Unit,
    onFieldChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = listOf(
        "Регистрация доступна до конца мероприятия",
        "Указать дату  и время закрытия регистрации",
        "Количество мест не ограничено",
        "Ограниченное количество мест",
        "Бесплатное посещение",
        "Платное посещение"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onRequirementChange(!requiresRegistration)
                }
        ) {
            Checkbox(
                checked = requiresRegistration,
                onCheckedChange = { onRequirementChange(it) },
                colors = CheckboxDefaults.colors(
                    checkedColor = orange,
                    checkmarkColor = Color.White,
                    uncheckedColor = Color.LightGray
                ),
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(8.dp))

            Text(
                text = "Требуется регистрация",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(16.dp))

        AnimatedContent(
            targetState = requiresRegistration,
            label = "registration-anim"
        ) { visible ->
            if (visible) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    IconHeader(
                        icon = painterResource(R.drawable.ic_registation_settings),
                        title = "Настройки регистрации"
                    )

                    items.forEach { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onFieldChange(item) }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = registrationFields[item] ?: false,
                                onCheckedChange = { onFieldChange(item) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = orange,
                                    checkmarkColor = Color.White,
                                    uncheckedColor = Color.LightGray
                                ),
                                modifier = Modifier.size(24.dp),
                            )

                            Spacer(Modifier.size(8.dp))

                            Text(
                                text = item,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun IconHeader(icon: Painter, title: String) {
    val headerColor = Color(0xFFD35400)
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = icon,
                contentDescription = "back_button",
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = title,
                color = headerColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                textAlign = TextAlign.Start
            )
        }
        Spacer(Modifier.size(8.dp))
        HorizontalDivider(
            thickness = 2.dp,
            color = headerColor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClosedEventView(vm: EventCreationViewModel) {
    var friendSearchLine by remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconHeader(
            icon = painterResource(R.drawable.ic_invite_friends),
            title = "Пригласить друзей"
        )

        OutlinedTextField(
            value = friendSearchLine,
            onValueChange = { friendSearchLine = it },
            placeholder = { Text("Найти друзей...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = false,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = orange,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = orange
            )
        )


    }
}

@Composable
private fun EventTypeToggle(
    isClosed: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE0E0E0))
    ) {
        val alignment = if (isClosed) Alignment.CenterStart else Alignment.CenterEnd

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .align(alignment)
                .clip(RoundedCornerShape(16.dp))
                .background(orange)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onToggle(true) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Закрытое",
                    color = if (isClosed) Color.White else Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onToggle(false) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Открытое",
                    color = if (!isClosed) Color.White else Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
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
fun InputIntField(
    title: String,
    placeholder: String,
    value: Int?,
    comment: String? = null,
    isObligatory: Boolean,
    onChange: (Int?) -> Unit
) {
    var text by remember(value) {
        mutableStateOf(value?.toString() ?: "")
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            Text(text = title, fontSize = 16.sp)
            if (isObligatory) {
                Text(text = " *", color = orange, fontSize = 16.sp)
            }
        }

        OutlinedTextField(
            value = text,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    text = newValue

                    onChange(
                        newValue.toIntOrNull()
                    )
                }
            },
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = orange,
                unfocusedBorderColor = Color.Gray,
                cursorColor = orange
            )
        )

        comment?.let {
            Text(
                text = it,
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                color = Color.Gray.copy(alpha = 0.7f)
            )
        }
    }
}
