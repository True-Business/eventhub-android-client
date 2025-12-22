package ru.truebusiness.liveposter_android_client.view.components

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.ui.theme.accentColor
import ru.truebusiness.liveposter_android_client.ui.theme.accentColorText
import ru.truebusiness.liveposter_android_client.utils.DateUtils.formatEventDate

@Composable
fun TinyEventCard(
    event: Event,
    withLabels: Boolean = true,
    isEditing: Boolean = false,
    onLockClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val context = LocalContext.current
    Column(modifier = Modifier.width(150.dp)) {
        Card(
            modifier = Modifier
                .aspectRatio(1f)
                .clickable { onClick() },
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
        ) {
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(event.posterUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = event.title,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)),
                    contentScale = ContentScale.Crop
                )


                androidx.compose.animation.AnimatedVisibility(
                    visible = isEditing,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    IconButton(
                        onClick = onLockClick,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(8.dp)
                            .height(65.dp)
                            .width(95.dp)
                            .background(
                                Color.Black.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(20.dp)
                            )
                    ) {
                        Icon(
                            tint = accentColor,
                            imageVector = Icons.Outlined.Lock, contentDescription = "lock",
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
        }


        if (withLabels) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(
                    event.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,


                    )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    formatEventDate(event.startDate),
                    fontSize = 12.sp,
                    color = accentColorText
                )
            }
        }
    }
}