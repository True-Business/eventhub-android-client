package ru.truebusiness.liveposter_android_client.view.organizations

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.data.Organization
import ru.truebusiness.liveposter_android_client.data.User

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrganizationPage(org: Organization) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val pageGradient = Brush.verticalGradient(
        0f to Color(0xFFCFCFCF),
        1f to Color(0xFFFF6D19)
    )

    Scaffold(
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .background(pageGradient)
        ) {

            //TODO fix отслеживание скролла
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .nestedScroll(scrollBehavior.nestedScrollConnection)

            ) {
                TopImageBlock(org)
                ContentBody(org = org)
            }
            AppBar(org.name, scrollBehavior)

        }

    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior
) {
    Box() {
        CenterAlignedTopAppBar(
            colors = TopAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Black,
                navigationIconContentColor = Color.Black,
                titleContentColor = Color.Black,
                actionIconContentColor = Color.Black,
            ),
            scrollBehavior = scrollBehavior,
            title = {

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(28.dp))
                        .background(Color.White.copy(alpha = 0.50f))
                    //.shadow(elevation = 6.dp, shape = RoundedCornerShape(28.dp))
                ) {
                    Text(
                        text = title,
                        modifier = Modifier.padding(horizontal = 28.dp, vertical = 8.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.Black
                    )
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(20.dp)),
            //.background(Color.White.copy(0.50f))
            navigationIcon = {
                IconButton(
                    onClick = {//TODO on click//
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

            actions = {
                IconButton(
                    onClick = {//TODO on click//
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
            }

        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black,
                            Color.Black.copy(alpha = 0.1f),
                            Color.Transparent,
                            Color.Transparent,
                        ),
                    )
                )
        )
    }

}

@Composable
fun TopImageBlock(org: Organization) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
    ) {
        AsyncImage(
            model = org.coverUrl,
            contentDescription = "Top image",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
fun ContentBody(org: Organization) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 30.dp)
    ) {
        LocationBlock(org)

        DescriptionBlock(org)

        Spacer(modifier = Modifier.height(16.dp))

        AdminsBlock(org.admins)

        Spacer(modifier = Modifier.height(16.dp))

        EventsBlock(org.events)

        Spacer(modifier = Modifier.height(16.dp))

        PicturesBlock(org)

    }
}

@Composable
fun PicturesBlock(org: Organization, onClick: (index: Int) -> Unit = {}) {
    Text(
        text = "Изображения",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = Color.White,
        modifier = Modifier.padding(horizontal = 36.dp)
    )
    Spacer(modifier = Modifier.height(12.dp))


    LazyRow(
        contentPadding = PaddingValues(start = 12.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(org.images) { url ->
            ImageCard(imageUrl = url)
        }
    }
}

@Composable
fun EventsBlock(events: List<Event>, onClick: (index: Int) -> Unit = {}) {
    Text(
        text = "Мероприятия",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = Color.White,
        modifier = Modifier.padding(horizontal = 24.dp)
    )

    Spacer(modifier = Modifier.height(12.dp))


    LazyRow(
        contentPadding = PaddingValues(start = 12.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(events) { ev ->
            EventCard(event = ev)
        }
    }
}

@Composable
fun AdminsBlock(admins: List<User>, onClick: () -> Unit = {}) {
    Surface(
        color = Color(0x83DFDFDF),
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 12.dp)
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
                color = Color.White
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
fun DescriptionBlock(org: Organization) {
    Surface(
        color = Color(0x83DFDFDF),
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
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = org.description,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 12.dp)

            )
        }
    }
}

@Composable
fun LocationBlock(org: Organization) {
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
        Text(
            text = org.address,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black,
            textDecoration = TextDecoration.Underline,
        )
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
fun EventCard(event: Event, withLabels: Boolean = true) {

    Column(modifier = Modifier.width(160.dp)) {
        Card(
            modifier = Modifier
                .size(width = 160.dp, height = 160.dp),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
        ) {
            AsyncImage(
                model = event.posterUrl,
                contentDescription = event.title,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)),
                contentScale = ContentScale.Crop
            )
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
                    event.startDate,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }
}


@Composable
fun ImageCard(imageUrl: String) {
    Card(
        modifier = Modifier
            .size(width = 320.dp, height = 200.dp),
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