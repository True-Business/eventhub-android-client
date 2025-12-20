package ru.truebusiness.liveposter_android_client.view.test

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.truebusiness.liveposter_android_client.repository.api.RetrofitInstance

/**
 * Тестовый экран для проверки работы Storage API.
 *
 * Позволяет:
 * - Выбрать и загрузить изображения (первое = cover)
 * - Просмотреть все загруженные изображения
 * - Загрузить только cover
 *
 * Использует тестовый userId: 045ea62e-259e-41c7-9da7-de8d44c65158
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageTestScreen() {
    val viewModel: StorageTestViewModel = viewModel()
    val context = LocalContext.current

    // ImageLoader с insecure OkHttpClient для self-signed сертификатов
    val insecureImageLoader = remember {
        ImageLoader.Builder(context)
            .okHttpClient(RetrofitInstance.insecureUploadClient)
            .build()
    }

    // Состояния
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val uploadedIds by viewModel.uploadedIds.collectAsState()
    val imageUrls by viewModel.imageUrls.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()

    // Выбранные файлы для загрузки
    var selectedUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    // Выбранный тип владельца
    var selectedOwnerType by remember { mutableStateOf(OwnerType.USER) }
    var ownerEntityId by remember { mutableStateOf("") }

    // Лаунчер для выбора изображений
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        selectedUris = uris
    }

    // Лаунчер для выбора одного изображения (cover)
    val singleImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.uploadCover(context, it, selectedOwnerType, ownerEntityId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Storage API Test") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF6600),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Секция информации
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Test User ID:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = StorageTestViewModel.TEST_USER_ID,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // Выбор типа владельца
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Owner Type",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OwnerType.entries.forEach { type ->
                                FilterChip(
                                    selected = selectedOwnerType == type,
                                    onClick = { selectedOwnerType = type },
                                    label = { Text(type.displayName) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = ownerEntityId,
                            onValueChange = { ownerEntityId = it },
                            label = { Text("Entity ID (optional)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                }
            }

            // Секция загрузки изображений
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Upload Images",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Выбранные изображения
                        if (selectedUris.isNotEmpty()) {
                            Text(
                                text = "Selected: ${selectedUris.size} image(s)",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "First image will be COVER",
                                fontSize = 12.sp,
                                color = Color(0xFFFF6600),
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(selectedUris.size) { index ->
                                    Box {
                                        AsyncImage(
                                            model = ImageRequest.Builder(context)
                                                .data(selectedUris[index])
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "Selected image $index",
                                            modifier = Modifier
                                                .size(80.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .border(
                                                    width = if (index == 0) 3.dp else 1.dp,
                                                    color = if (index == 0) Color(0xFFFF6600) else Color.Gray,
                                                    shape = RoundedCornerShape(8.dp)
                                                ),
                                            contentScale = ContentScale.Crop
                                        )
                                        if (index == 0) {
                                            Text(
                                                text = "COVER",
                                                fontSize = 8.sp,
                                                color = Color.White,
                                                modifier = Modifier
                                                    .align(Alignment.TopCenter)
                                                    .background(
                                                        Color(0xFFFF6600),
                                                        RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp)
                                                    )
                                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { imagePickerLauncher.launch("image/*") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Select Images")
                            }

                            Button(
                                onClick = {
                                    viewModel.uploadImages(context, selectedUris, selectedOwnerType, ownerEntityId)
                                    selectedUris = emptyList()
                                },
                                enabled = selectedUris.isNotEmpty() && !isLoading,
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF6600)
                                )
                            ) {
                                Text("Upload")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = { singleImagePickerLauncher.launch("image/*") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isLoading
                        ) {
                            Text("Upload Cover Only")
                        }
                    }
                }
            }

            // Секция получения изображений
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Get Images",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.loadImages(selectedOwnerType, ownerEntityId) },
                                enabled = !isLoading,
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4CAF50)
                                )
                            ) {
                                Text("Load All")
                            }

                            Button(
                                onClick = { viewModel.loadCoverOnly(selectedOwnerType, ownerEntityId) },
                                enabled = !isLoading,
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2196F3)
                                )
                            ) {
                                Text("Load Cover")
                            }
                        }
                    }
                }
            }

            // Статус и ошибки
            item {
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFFFF6600))
                    }
                }

                statusMessage?.let { message ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                    ) {
                        Text(
                            text = message,
                            modifier = Modifier.padding(16.dp),
                            color = Color(0xFF2E7D32)
                        )
                    }
                }

                error?.let { errorMsg ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                    ) {
                        Text(
                            text = "Error: $errorMsg",
                            modifier = Modifier.padding(16.dp),
                            color = Color(0xFFC62828)
                        )
                    }
                }
            }

            // Результат загрузки - IDs
            if (uploadedIds.isNotEmpty()) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Uploaded IDs (${uploadedIds.size})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            uploadedIds.forEach { id ->
                                Text(
                                    text = id,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            // Загруженные изображения
            imageUrls?.let { urls ->
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Loaded Images",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Total: ${urls.totalCount()} | Cover: ${if (urls.coverUrl != null) "Yes" else "No"}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Cover
                            urls.coverUrl?.let { coverUrl ->
                                Text(
                                    text = "Cover:",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(coverUrl)
                                        .crossfade(true)
                                        .build(),
                                    imageLoader = insecureImageLoader,
                                    contentDescription = "Cover image",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(
                                            3.dp,
                                            Color(0xFFFF6600),
                                            RoundedCornerShape(8.dp)
                                        ),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            // Other images
                            if (urls.imageUrls.isNotEmpty()) {
                                Text(
                                    text = "Other Images (${urls.imageUrls.size}):",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(urls.imageUrls) { url ->
                                        AsyncImage(
                                            model = ImageRequest.Builder(context)
                                                .data(url)
                                                .crossfade(true)
                                                .build(),
                                            imageLoader = insecureImageLoader,
                                            contentDescription = "Image",
                                            modifier = Modifier
                                                .size(120.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .border(
                                                    1.dp,
                                                    Color.Gray,
                                                    RoundedCornerShape(8.dp)
                                                ),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Spacer at bottom
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

