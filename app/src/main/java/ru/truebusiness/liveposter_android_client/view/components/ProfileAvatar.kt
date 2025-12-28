package ru.truebusiness.liveposter_android_client.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import ru.truebusiness.liveposter_android_client.repository.api.RetrofitInstance

private val orange = Color(0xFFFF6600)

/**
 * Переиспользуемый компонент аватара профиля с поддержкой:
 * - insecureImageLoader для self-signed сертификатов
 * - Плавная загрузка: CircularProgressIndicator → фото/заглушка
 * - Заглушка Icons.Default.AccountCircle при отсутствии фото или ошибке
 *
 * @param avatarUrl URL фото пользователя (пустая строка = заглушка)
 * @param size размер аватара
 * @param onClick обработчик клика (null = не кликабельный)
 * @param showPlaceholderOnError показывать заглушку при ошибке загрузки
 */
@Composable
fun ProfileAvatar(
    avatarUrl: String,
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    onClick: (() -> Unit)? = null,
    showPlaceholderOnError: Boolean = true
) {
    val context = LocalContext.current

    // ImageLoader с insecure OkHttpClient для self-signed сертификатов
    val insecureImageLoader = remember {
        ImageLoader.Builder(context)
            .okHttpClient(RetrofitInstance.insecureUploadClient)
            .build()
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.White)
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (avatarUrl.isNotEmpty()) {
            SubcomposeAsyncImage(
                model = avatarUrl,
                imageLoader = insecureImageLoader,
                contentDescription = "Avatar",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = orange,
                            modifier = Modifier.size(size / 3)
                        )
                    }
                },
                error = {
                    if (showPlaceholderOnError) {
                        PlaceholderIcon()
                    }
                }
            )
        } else {
            PlaceholderIcon()
        }
    }
}

@Composable
private fun PlaceholderIcon() {
    Icon(
        imageVector = Icons.Default.AccountCircle,
        contentDescription = "Avatar",
        modifier = Modifier.fillMaxSize(),
        tint = orange
    )
}

