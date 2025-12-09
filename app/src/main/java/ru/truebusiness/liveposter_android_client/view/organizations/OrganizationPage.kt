package ru.truebusiness.liveposter_android_client.view.organizations

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.data.Organization
import ru.truebusiness.liveposter_android_client.data.User
import java.util.UUID
import ru.truebusiness.liveposter_android_client.ui.theme.accentColor
import ru.truebusiness.liveposter_android_client.ui.theme.accentColorText
import ru.truebusiness.liveposter_android_client.ui.theme.pageGradient
import ru.truebusiness.liveposter_android_client.view.components.FullScreenImageViewer
import ru.truebusiness.liveposter_android_client.view.components.TinyEventCard
import ru.truebusiness.liveposter_android_client.view.viewmodel.OrganizationViewModel


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrganizationPage(
    orgViewMod: OrganizationViewModel,
    navigator: NavHostController,
    organizationId: UUID
) {
    LaunchedEffect(organizationId) {
        orgViewMod.loadOrRefreshOrganization(organizationId)
    }

    DisposableEffect(Unit) {
        onDispose {
            orgViewMod.clearState()
        }
    }

    val state by orgViewMod.organizationState.collectAsState()

    when (val it = state) {
        is OrganizationViewModel.OrganizationState.Success -> OrganizationPageContent(
            org = it.org,
            orgViewMod = orgViewMod,
            navigator = navigator
        )

        is OrganizationViewModel.OrganizationState.Loading -> {
            OrganizationPageSkeleton()
        }

        is OrganizationViewModel.OrganizationState.Error -> {
            OrganizationPageError(message = it.message) {
                // TODO: orgViewMod.retry()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrganizationPageContent(
    org: Organization,
    orgViewMod: OrganizationViewModel,
    navigator: NavHostController
) {
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var isEditing by remember { mutableStateOf(false) }


// Local editable states (mocking local edits)
    var name by remember { mutableStateOf(org.name) }
    var description by remember { mutableStateOf(org.description) }
    var address by remember { mutableStateOf(org.address) }


// Mutable lists for images and events so we can delete / modify locally
    val admins = remember { org.admins.toMutableStateList() }
    val images = remember { org.images.toMutableStateList() }
    val events = remember { org.events.toMutableStateList() }

    val context = LocalContext.current
    val isAdmin by orgViewMod.isMy.collectAsState()



    Scaffold(
        topBar = {
            AppBar(
                scrollBehavior,
                isEditing = isEditing,
                onEditToggle = {
                    isEditing = !isEditing
                },
                isAdmin = isAdmin,
                onBack = { navigator.navigate("organizations") },
                onSave = {
                    isEditing = false
                    orgViewMod.saveOrganizationChanges(
                        onSuccess = {
                            Log.d("OrganizationPage", "Organization saved successfully")
                        },
                        onError = { error ->
                            Toast.makeText(context, "Ошибка сохранения: $error", Toast.LENGTH_LONG).show()
                        }
                    )
                },
                onCancel = {
                    isEditing = false
                    name = org.name
                    description = org.description
                    address = org.address

                    images.clear()
                    images.addAll(org.images)
                    admins.clear()
                    admins.addAll(org.admins)
                    events.clear()
                    events.addAll(org.events)


                }
            )
        }
    ) {_ ->

        Box(
            modifier = Modifier
                .background(pageGradient)
                .fillMaxSize()
        ) {

            Column(
                Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .verticalScroll(scrollState)

            ) {
                TopImageBlock(
                    name = name,
                    onNameChange = { name = it }, org, isEditing
                )
                ContentBody(
                    description = description,
                    onDescriptionChange = { description = it },
                    address = address,
                    onAddressChange = { address = it },
                    images = images,
                    onDeleteImage = { idx -> images.removeAt(idx) },
                    events = events,
                    onLockEvent = { ev ->
                        Toast.makeText(
                            context,
                            "Mock: lock action for ${ev.title}",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    admins = admins,
                    navigator = navigator,
                    isEditing = isEditing
                )
            }

        }

    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    isEditing: Boolean,
    isAdmin: Boolean,
    onBack: () -> Unit,
    onEditToggle: () -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Box() {

        AnimatedVisibility(
            visible = isEditing,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .background(

                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF000000),
                                Color(0x00FFFFFF)
                            ),
                        )
                    )
            )
        }

        CenterAlignedTopAppBar(
            colors = TopAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
                navigationIconContentColor = Color.Black,
                titleContentColor = Color.Black,
                actionIconContentColor = Color.Black,
            ),
            scrollBehavior = if (!isEditing) {
                scrollBehavior
            } else {
                TopAppBarDefaults.pinnedScrollBehavior()
            },
            title = {
                AnimatedVisibility(
                    visible = isEditing,
                    enter = slideInVertically(
                        initialOffsetY = { -it - 30 }
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { -it - 30 }
                    )
                ) {
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
                            text = "редактирование"
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(20.dp)),
            navigationIcon = if (!isEditing) {
                {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.50f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            } else {
                {
                    IconButton(
                        onClick = {
                            onCancel()
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.50f))
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "back"
                        )
                    }
                }
            },

            actions = if (isAdmin) {
                {
                    if (!isEditing) {
                        IconButton(
                            onClick = {
                                onEditToggle()
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.50f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "edit"
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { onSave() },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.50f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "save"
                            )
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
fun TopImageBlock(
    name: String,
    onNameChange: (String) -> Unit, org: Organization, isEditing: Boolean
) {
    val height = 360.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        AsyncImage(
            model = org.coverUrl,
            contentDescription = "Top image",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.1f),
                            Color.Black,
                        ),
                    )
                ),
            contentAlignment = Alignment.BottomStart
        ) {
            if (isEditing) {
                BasicTextField(
                    value = name,
                    onValueChange = { name -> onNameChange(name) },
                    modifier = Modifier
                        .padding(12.dp),
                    textStyle = TextStyle(
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    ),
                ) { inner ->
                    Box(modifier = Modifier) {
                        inner()
                    }
                }
            } else {
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = org.name,
                    color = Color.White,
                    fontSize = 44.sp, // Можешь настроить размер шрифта
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )
            }
        }
    }
}


@Composable
fun ContentBody(
    description: String,
    onDescriptionChange: (String) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit,
    images: MutableList<String>,
    onDeleteImage: (index: Int) -> Unit,
    events: MutableList<Event>,
    onLockEvent: (Event) -> Unit,
    admins: MutableList<User>,
    navigator: NavHostController?,
    isEditing: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 30.dp)
    ) {
        if (address.isNotBlank()) {
            LocationBlock(
                address = address,
                onAddressChange = onAddressChange,
                isEditing = isEditing
            )
        }

        if (description.isNotBlank()) {
            DescriptionBlock(
                description = description,
                onDescriptionChange = onDescriptionChange,
                isEditing = isEditing
            )
        }


        if (admins.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            AdminsBlock(admins, onClick = { navigator?.navigate("organizationAdmins") })
        }

        if (events.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            EventsBlock(navigator, events = events, isEditing = isEditing, onLock = onLockEvent)
        }


        if (images.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            PicturesBlock(images = images, onDeleteImage = onDeleteImage, isEditing = isEditing)
        }


    }
}

@Composable
fun PicturesBlock(
    images: MutableList<String>,
    onDeleteImage: (index: Int) -> Unit,
    isEditing: Boolean
) {
    var showFullScreenViewer by remember { mutableStateOf(false) }
    var selectedImageIndex by remember { mutableStateOf(0) }


    Text(
        text = "Изображения",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = accentColorText,
        modifier = Modifier.padding(horizontal = 24.dp)
    )
    Spacer(modifier = Modifier.height(12.dp))


    LazyRow(
        contentPadding = PaddingValues(start = 12.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(images.size) { index ->
            val url = images[index]
            Box {
                ImageCard(
                    imageUrl = url,
                    onClick = {
                        selectedImageIndex = index
                        showFullScreenViewer = true
                    }
                )


                AnimatedVisibility(
                    visible = isEditing,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    IconButton(
                        onClick = { onDeleteImage(index) },
                        modifier = Modifier
                            .align(Alignment.Center)
                            .width(95.dp)
                            .height(65.dp)
                            .background(
                                Color.Black.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(20.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "delete",
                            tint = accentColor,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
        }
    }


    if (showFullScreenViewer) {
// Use provided list (images) so deletions are reflected
        FullScreenImageViewer(
            imageUrls = images,
            initialIndex = selectedImageIndex,
            onDismiss = { showFullScreenViewer = false }
        )
    }
}

@Composable
fun EventsBlock(
    navigator : NavHostController?,
    events: List<Event>, isEditing: Boolean, onLock: (Event) -> Unit) {
    Text(
        text = "Мероприятия",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = accentColorText,
        modifier = Modifier.padding(horizontal = 24.dp)
    )


    Spacer(modifier = Modifier.height(12.dp))


    LazyRow(
        contentPadding = PaddingValues(start = 12.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(events) { ev ->
            TinyEventCard(
                event = ev,
                withLabels = true,
                isEditing = isEditing,
                onLockClick = { onLock(ev) },
                onClick = { navigator?.navigate("event/${ev.id}") })
        }
    }
}

@Composable
fun AdminsBlock(admins: List<User>, onClick: () -> Unit = {}) {
    Surface(
        color = androidx.compose.ui.graphics.Color(0x83DFDFDF),
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 12.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Администраторы",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = accentColorText
            )


            if (admins.isNotEmpty()) {
                OverlappingAdminAvatars(adminAvatars = admins.map { it.coverUrl })
            }
        }
    }
}


@Composable
fun OverlappingAdminAvatars(adminAvatars: List<String>) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        val visibleAvatars = adminAvatars.take(3)
        val avatarCount = visibleAvatars.size


        visibleAvatars.forEachIndexed { index, avatarUrl ->
            Box(
                modifier = Modifier
                    .zIndex(index.toFloat())
                    .offset(x = ((avatarCount - 1 - index) * 24).dp)
            ) {
                AdminAvatar(coverUrl = avatarUrl)
            }
        }
    }
}


@Composable
fun DescriptionBlock(
    description: String,
    onDescriptionChange: (String) -> Unit,
    isEditing: Boolean
) {
    Surface(
        color = androidx.compose.ui.graphics.Color(0x83DFDFDF),
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Описание",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = accentColorText,
            )
            Spacer(modifier = Modifier.height(8.dp))


            if (isEditing) {
                BasicTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.ui.graphics.Color.Black
                    ),
                ) { inner ->
                    Box(modifier = Modifier) {
                        inner()
                    }
                }
            } else {
                Text(
                    text = description,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    }
}

@Composable
fun LocationBlock(address: String, onAddressChange: (String) -> Unit, isEditing: Boolean) {
    Row(
        modifier = Modifier.padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location",
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(8.dp))


        if (isEditing) {
            BasicTextField(
                value = address,
                onValueChange = onAddressChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = androidx.compose.ui.graphics.Color.Black,
                    textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                )
            ) { inner ->
                Box {
                    inner()
                }
            }
        } else {
            Text(
                text = address,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = androidx.compose.ui.graphics.Color.Black,
                textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline,
            )
        }
    }
}

@Composable
fun AdminAvatar(coverUrl: String) {
    AsyncImage(
        model = coverUrl,
        contentDescription = "avatar",
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ImageCard(imageUrl: String, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .size(width = 320.dp, height = 200.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}