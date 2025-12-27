package ru.truebusiness.liveposter_android_client.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import ru.truebusiness.liveposter_android_client.R
import ru.truebusiness.liveposter_android_client.repository.api.RetrofitInstance
import ru.truebusiness.liveposter_android_client.view.components.AppNavigationBar
import ru.truebusiness.liveposter_android_client.view.components.InfoSurface
import ru.truebusiness.liveposter_android_client.view.viewmodel.Organization
import ru.truebusiness.liveposter_android_client.view.viewmodel.ProfileViewModel

private val orange = Color(0xFFFF6600)

@Composable
fun ProfilePage(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val uiState by profileViewModel.uiState
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            profileViewModel.uploadUserPhoto(context, it)
        }
    }

    // Показываем ошибку в Snackbar
    LaunchedEffect(uiState.photoUploadError) {
        uiState.photoUploadError?.let { error ->
            snackbarHostState.showSnackbar(error)
            profileViewModel.clearPhotoUploadError()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    0f to orange,
                    0.44f to Color.White,
                    1f to Color.White
                )
            )
    ) {
        Scaffold(
            bottomBar = {
                AppNavigationBar(
                    navController,
                    selectedRoute = "profile-settings"
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                }
            },
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(Modifier.height(8.dp))
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.back_buttom),
                        contentDescription = "back buttom",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { navController.navigate("profile-settings") },
                        contentScale = ContentScale.Crop
                    )
                    ProfileIconView(
                        name = uiState.name,
                        username = uiState.username,
                        avatarUrl = uiState.avatarUrl,
                        isUploading = uiState.isUploadingPhoto,
                        onAddPhotoClick = { imagePickerLauncher.launch("image/*") }
                    )

                    AboutYourselfView(uiState.about)

                    StatisticsView(uiState.eventsCreated, uiState.eventsVisited)
                }

                OrganizationsView(
                    uiState.organizations,
                    uiState.currentOrganizationIndex,
                    profileViewModel::prevOrganization,
                    profileViewModel::nextOrganization
                )
            }
        }
    }
}

@Composable
fun OrganizationsView(
    organizations: List<Organization>,
    currentOrganizationIndex: Int,
    prevOrganization: () -> Unit,
    nextOrganization: () -> Unit
) {
    val currentOrg = organizations.getOrNull(currentOrganizationIndex)

    if (currentOrg != null) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (organizations.size > 1) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Previous organization",
                    tint = Color(0xFFFF6600),
                    modifier = Modifier
                        .size(36.dp)
                        .clickable { prevOrganization() }
                )
            } else {
                Spacer(Modifier.width(36.dp))
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(width = 1.dp, Color(0xFFFF6600), RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = currentOrg.imageUrl,
                                contentDescription = "Avatar",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = currentOrg.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF6600)
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "${currentOrg.membersCount}",
                            color = Color(0xFFFF6600),
                            fontSize = 14.sp
                        )
                        Icon(
                            imageVector = Icons.Default.AccountBox,
                            contentDescription = null,
                            tint = Color(0xFFFF6600),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = currentOrg.description,
                        fontSize = 13.sp
                    )
                }
            }

            if (organizations.size > 1) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Next organization",
                    tint = Color(0xFFFF6600),
                    modifier = Modifier
                        .size(36.dp)
                        .clickable { nextOrganization() }
                )
            } else {
                Spacer(Modifier.width(36.dp))
            }
        }
    }
}

@Composable
fun StatisticsView(eventsCreated: Int, eventsVisited: Int) {
    InfoSurface {
        ProfileSection(
            title = "Мероприятия",
            content = "Организовано: $eventsCreated\nПосещено: $eventsVisited"
        )
    }
}

@Composable
fun AboutYourselfView(description: String) {
    InfoSurface {
        ProfileSection(
            title = "О себе",
            content = description
        )
    }
}

@Composable
fun ProfileIconView(
    name: String,
    username: String,
    avatarUrl: String,
    isUploading: Boolean = false,
    onAddPhotoClick: (() -> Unit)? = null
) {
    val context = LocalContext.current

    // ImageLoader с insecure OkHttpClient для self-signed сертификатов
    val insecureImageLoader = remember {
        ImageLoader.Builder(context)
            .okHttpClient(RetrofitInstance.insecureUploadClient)
            .build()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp),
            contentAlignment = Alignment.Center
        ) {
            // Аватар
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color.White)
                    .then(
                        if (onAddPhotoClick != null && !isUploading) {
                            Modifier.clickable { onAddPhotoClick() }
                        } else {
                            Modifier
                        }
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
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        },
                        error = {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Avatar",
                                modifier = Modifier.fillMaxSize(),
                                tint = orange
                            )
                        }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Avatar",
                        modifier = Modifier.fillMaxSize(),
                        tint = orange
                    )
                }
            }

            // Индикатор загрузки (при загрузке нового фото)
            if (isUploading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            // Иконка камеры для добавления фото
            if (onAddPhotoClick != null && !isUploading) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(orange)
                        .clickable { onAddPhotoClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Добавить фото",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        Text(name, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text(username, fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
private fun ProfileSection(title: String, content: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(title, color = Color(0xFFFF6600), fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(content, fontSize = 14.sp)
    }
}