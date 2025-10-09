package ru.truebusiness.liveposter_android_client.view.registration

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import kotlinx.coroutines.delay
import ru.truebusiness.liveposter_android_client.view.components.GradientButton
import ru.truebusiness.liveposter_android_client.view.viewmodel.AuthViewModel

@Composable
fun RegistrationPageEmailVerification(
    viewModel: AuthViewModel,
    navController: NavController,
    userId: String
) {
    var verificationCode by remember { mutableStateOf("") }
    var isResendEnabled by remember { mutableStateOf(true) }
    var countdown by remember { mutableIntStateOf(60) }
    var showError by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val email by viewModel.email.collectAsState(initial = null)

    // Таймер для кнопки переотправки
    LaunchedEffect(isResendEnabled, countdown) {
        if (!isResendEnabled && countdown > 0) {
            delay(1000)
            countdown--
        } else if (countdown == 0) {
            isResendEnabled = true
            countdown = 60
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Подтвердите email",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Мы отправили код на $email",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = verificationCode,
            onValueChange = {
                verificationCode = it
                showError = false
            },
            label = { Text(text = "Код подтверждения", color = Color.Black) },
            isError = showError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Gray
            )
        )

        if (showError) {
            Text(
                text = "Неверный код подтверждения",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        GradientButton(text = "Подтвердить") {
            if (verificationCode.length == 4) {
                viewModel.verifyCode(verificationCode) { returnedId ->
                    val nextId = returnedId ?: userId
                    navController.navigate("user_personal_data/$nextId")
                }
            } else {
                showError = true
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                if (isResendEnabled) {
                    viewModel.sendCode(userId)
                    Toast.makeText(context, "Код отправлен повторно", Toast.LENGTH_SHORT).show()
                    isResendEnabled = false
                }
            },
            enabled = isResendEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isResendEnabled) {
                Text(text = "Отправить код повторно", color = Color.Black)
            } else {
                Text(text = "Повторная отправка через $countdown сек", color = Color.Black)
            }
        }
    }
}