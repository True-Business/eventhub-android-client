package ru.truebusiness.liveposter_android_client.view.registration

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.truebusiness.liveposter_android_client.data.dto.RegistrationStatus
import ru.truebusiness.liveposter_android_client.view.components.GradientButton
import ru.truebusiness.liveposter_android_client.view.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationPagePersonalInfo(
    vm: AuthViewModel,
    navController: NavController,
    userId: String
) {
    val TAG = "RegistrationPagePersonalInfo"

    var displayName by remember { mutableStateOf("") }
    var shortId by remember { mutableStateOf("") }
    var isShortIdValid by remember { mutableStateOf(true) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Персональные данные",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = { Text("Как вас зовут") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black.copy(alpha = 0.6f),
                focusedBorderColor = Color.Black
            )
        )

        OutlinedTextField(
            value = shortId,
            onValueChange = {
                shortId = it
                isShortIdValid = it.matches(Regex("^[a-zA-Z0-9]+$"))
            },
            label = { Text("Короткое имя (например, @ivanov)") },
            isError = !isShortIdValid,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black.copy(alpha = 0.6f),
                focusedBorderColor = Color.Black,
                errorBorderColor = Color.Red
            ),
            prefix = { Text("@") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                autoCorrect = false
            )
        )

        if (!isShortIdValid) {
            Text(
                text = "Имя может содержать только буквы, цифры и _",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        GradientButton(text = "Продолжить") {
            if (displayName.isBlank()) {
                Toast.makeText(context, "Введите ваше имя", Toast.LENGTH_SHORT).show()
                return@GradientButton
            }
            if (!isShortIdValid || shortId.isBlank()) {
                Toast.makeText(context, "Некорректное имя пользователя", Toast.LENGTH_SHORT).show()
                return@GradientButton
            }

            /*
            * В случае успеха здесь переход на navController.navigate("main") не делается.
            * Это сделано по следующей причине:
            *
            * в методе ru.truebusiness.liveposter_android_client.view.viewmodel.AuthViewModel.postRegister
            * мы выставляем в dataStore что пользователь залогинился (prefs[IS_LOGGED_IN] = true)
            * На это действие тригерится LaunchedEffect в ru/truebusiness/liveposter_android_client/NavController.kt:105,
            * который уже перебросит пользователя на главную страницу
            * */
            vm.postRegister(userId, displayName, shortId) { status, reason ->
                if (status == RegistrationStatus.ERROR.name) {
                    Log.e(TAG, "Couldn't post register user: $reason")
                    Toast
                        .makeText(
                            context,
                            "Не удалось сохранить данные: $reason",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                    return@postRegister
                }
            }
        }
    }
}