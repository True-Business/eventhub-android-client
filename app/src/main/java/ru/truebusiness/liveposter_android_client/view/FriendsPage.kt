package ru.truebusiness.liveposter_android_client.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import ru.truebusiness.liveposter_android_client.R
import ru.truebusiness.liveposter_android_client.view.components.AppNavigationBar
import ru.truebusiness.liveposter_android_client.view.viewmodel.AuthViewModel
import ru.truebusiness.liveposter_android_client.view.viewmodel.FriendsViewModel
import java.util.UUID

private val orange = Color(0xFFFF6600)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsPage(
    navController: NavController,
    friendsViewModel: FriendsViewModel = viewModel()
) {

    // 0 - Все, 1 - Входящие, 2 - Исходящие
    val selectedTab = friendsViewModel.selectedTab
    val currentUser by friendsViewModel.currentUser.collectAsState(null)


    val userName = currentUser?.username ?: ""
    val avatarUrl = currentUser?.photoUrl

    val orange = Color(0xFFFF6600)

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
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent
                    ),
                    actions = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(65.dp)
                                .width(200.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(55.dp)
                                    .height(45.dp)
                                    .padding(start = 10.dp)
                                    .background(color = Color.White, shape = CircleShape)
                                    .clickable(onClick = { navController.navigate("profile-settings") }),
                                contentAlignment = Alignment.Center
                            ) {
                                if (!avatarUrl.isNullOrEmpty()) {
                                    AsyncImage(
                                        model = avatarUrl,
                                        contentDescription = "Avatar",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = "Avatar",
                                        modifier = Modifier.fillMaxSize(),
                                        tint = Color(0xFFFF6600)
                                    )
                                }
                            }

                            Text(
                                text = userName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                modifier = Modifier.padding(start = 18.dp)
                            )

                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(end = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(
                                    onClick = {

                                    },
                                    modifier = Modifier
                                        .height(40.dp)
                                        .width(40.dp),
                                    content = {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_add),
                                            contentDescription = "Add",
                                            modifier = Modifier.size(36.dp)
                                        )
                                    }
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = {
                AppNavigationBar(
                    navController,
                    "friends",
                    (currentUser != null && currentUser?.id != "anonymous-user-id")
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // ВЕРХНИЕ КНОПКИ
                FriendsTabs(selectedTab, friendsViewModel::onTabSelected)

                Spacer(modifier = Modifier.height(12.dp))

                // ПОИСКОВАЯ СТРОКА
                when (selectedTab) {
                    0 -> AllView(friendsViewModel)
                    1 -> IncomingView(friendsViewModel)
                    2 -> OutgoingView(friendsViewModel)
                }
            }
        }
    }

}

@Composable
fun OutgoingView(vm: FriendsViewModel) {
    { }
}

@Composable
fun IncomingView(vm: FriendsViewModel) {
    {}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllView(vm: FriendsViewModel) {
    val searchQuery by vm.searchQuery.observeAsState("")
    val userFriends = vm.friendsList

    OutlinedTextField(
        value = searchQuery,
        onValueChange = { vm.onSearchQueryChanged(it) },
        placeholder = { Text("Найти друзей...") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(24.dp),
        singleLine = true,
        isError = searchQuery.isNotEmpty() && searchQuery.length < 3,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = orange,
            unfocusedBorderColor = Color(0xFFFFD1B3),
            cursorColor = orange
        )
    )
    if (searchQuery.isNotEmpty() && searchQuery.length < 3) {
        Text(
            text = "Минимум 3 символа",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier.padding(horizontal = 42.dp)
    ) {
        Text(
            text = "№",
            color = Color.Black,
            fontSize = 16.sp,
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = "Имя пользователя",
            color = Color.Black,
            fontSize = 16.sp,
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxSize()
    ) {
        itemsIndexed(userFriends) { index, friend ->
            FriendItem(
                number = index + 1,
                name = friend.username
            )
            Spacer(Modifier.height(4.dp))
        }
    }
    LaunchedEffect(Unit) {
        if (userFriends.isEmpty()) {
            vm.loadFriends()
        }
    }
}


@Composable
private fun FriendsTabs(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("Все", "Входящие", "Исходящие")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFFFD1B3))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        tabs.forEachIndexed { index, title ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (selectedTab == index)
                            orange
                        else
                            Color.Transparent
                    )
                    .clickable { onTabSelected(index) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendItem(number: Int, name: String) {
    Button(
        onClick = {},
        shape = RoundedCornerShape(24.dp),
        colors = ButtonColors(
            containerColor = Color(0xFFFFD1B3),
            contentColor = Color.Black,
            disabledContainerColor = Color(0xFFFFD1B3),
            disabledContentColor = Color.Black
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$number.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.width(32.dp)
            )

            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    FriendsTabs(2, {})
}