package ru.truebusiness.liveposter_android_client.view

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.truebusiness.liveposter_android_client.view.viewmodel.FriendsViewModel
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsPage(
    navController: NavController,
    friendsViewModel: FriendsViewModel = viewModel()
) {

    // 0 - Все, 1 - Входящие, 2 - Исходящие
    var selectedTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    val userFriends by friendsViewModel.friends.observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(
            title = { Text("Мои друзья") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // ВЕРХНИЕ КНОПКИ
        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { friendsViewModel.onTabSelected(0) }, text = { Text("Все") })
            Tab(selected = selectedTab == 1, onClick = { friendsViewModel.onTabSelected(1) }, text = { Text("Входящие") })
            Tab(selected = selectedTab == 2, onClick = { friendsViewModel.onTabSelected(2) }, text = { Text("Исходящие") })
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ПОИСКОВАЯ СТРОКА
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { friendsViewModel.onSearchQueryChanged(it) },
            label = { Text("Поиск друзей") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = searchQuery.isNotEmpty() && searchQuery.length < 3,
        )
        if (searchQuery.isNotEmpty() && searchQuery.length < 3) {
            Text(
                text = "Минимум 3 символа",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(userFriends) { friend ->
                FriendItem(friend.username)
            }
        }
    }

    LaunchedEffect(Unit) {
        if (userFriends.isEmpty()) {
            friendsViewModel.loadFriends(UUID.randomUUID())
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendItem(name: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(40.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(name, style = MaterialTheme.typography.bodyLarge)
    }
}