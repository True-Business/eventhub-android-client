package ru.truebusiness.liveposter_android_client.view.organizations

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import ru.truebusiness.liveposter_android_client.data.User
import ru.truebusiness.liveposter_android_client.ui.theme.pageGradient
import ru.truebusiness.liveposter_android_client.view.viewmodel.OrganizationViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AdminsScreen(
    orgViewModel: OrganizationViewModel,
    navigator: NavHostController,
    onSaveEditing: (List<User>) -> Unit = {}
) {
    val organization by orgViewModel.currentOrganization.collectAsState()

    val admins = remember { organization.admins.toMutableStateList() }
    val isEditing = remember { mutableStateOf(false) }
    val isAdmin by orgViewModel.isMy.collectAsState()

    Scaffold(
        topBar = {
            AppBar(
                admins,
                isEditing,
                navigator = navigator,
                isAdmin,
                onSaveEditing,
            )
        },
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (admins.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(pageGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Список пуст",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .background(pageGradient)
                        .fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        itemsIndexed(admins, key = { _, u -> u.userName }) { index, user ->
                            AdminListItem(admins, user, index, isEditing)
                        }
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppBar(
    admins: List<User>,
    isEditing: MutableState<Boolean>,
    navigator: NavHostController,
    isAdmin: Boolean,
    onSaveEditing: (List<User>) -> Unit
) {
    Box() {
        CenterAlignedTopAppBar(
            colors = TopAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
                navigationIconContentColor = Color.Black,
                titleContentColor = Color.Black,
                actionIconContentColor = Color.Black,
            ),
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            title = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.50f))
                        .height(50.dp)
                        .padding(12.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        text = if (isEditing.value) "редактирование" else "администраторы"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(20.dp)),
            navigationIcon = {
                IconButton(
                    onClick = {
                        navigator.popBackStack()
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.50f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back"
                    )
                }
            },

            actions = if (isAdmin) {
                {
                    if (isEditing.value) {
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.50f))
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Добавить")
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        IconButton(
                            onClick = {
                                onSaveEditing(admins)
                                isEditing.value = false
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.50f))
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Сохранить")
                        }
                    } else {
                        IconButton(
                            onClick = { isEditing.value = true }, modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.50f))
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Редактировать")
                        }
                    }
                }
            } else {
                {}
            }

        )
    }

}

@Composable
fun AdminListItem(
    adminsState: SnapshotStateList<User>,
    user: User,
    index: Int,
    isEditing: MutableState<Boolean>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.coverUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "avatar",
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterVertically)
                    .clip(CircleShape)
                    .shadow(2.dp, CircleShape, spotColor = Color.Black),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(40.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = user.userName,
                        modifier = Modifier.padding(vertical = 14.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )

                    AnimatedVisibility(
                        visible = isEditing.value,
                        enter = fadeIn(animationSpec = tween(180)),
                        exit = fadeOut(animationSpec = tween(150))
                    ) {
                        IconButton(onClick = {
                            adminsState.removeAt(index)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Удалить"
                            )
                        }
                    }
                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(5.dp),
                    thickness = 1.dp,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }
    }
}

