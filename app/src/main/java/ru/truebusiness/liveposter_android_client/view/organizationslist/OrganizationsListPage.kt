package ru.truebusiness.liveposter_android_client.view.organizationslist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ru.truebusiness.liveposter_android_client.ui.theme.pageGradient
import ru.truebusiness.liveposter_android_client.view.organizationslist.components.FilterTabsSection
import ru.truebusiness.liveposter_android_client.view.components.OrganizationCard
import ru.truebusiness.liveposter_android_client.view.organizationslist.components.TopBarSection
import ru.truebusiness.liveposter_android_client.view.viewmodel.OrganizationsViewModel

enum class OrganizationTab { ALL, SUBSCRIPTIONS, MINE }

@Composable
fun OrganizationsListPage(vm: OrganizationsViewModel, navigator: NavHostController) {
    val state by vm.state.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // триггер дозагрузки за ~3 элемента до конца
    val shouldLoadMore by remember {
        derivedStateOf {
            val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = listState.layoutInfo.totalItemsCount
            total > 0 && last >= total - 4 && !state.endReached && !state.isPaging
        }
    }
    LaunchedEffect(shouldLoadMore) { if (shouldLoadMore) vm.loadNextPage() }

    if (state.isInitialLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    LaunchedEffect(state.selectedTab) {
        listState.animateScrollToItem(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(pageGradient)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        TopBarSection(
            searchText = "",                    // пустая строка
            onSearchChange = {},                // no-op
            onBackClick = { navigator.navigate("main") },
            onFilterClick = {}                  // no-op
        )

        FilterTabsSection(
            selected = state.selectedTab,
            onSelected = { tab ->
                if (tab == state.selectedTab) {
                    scope.launch { listState.animateScrollToItem(0) }
                } else {
                    vm.onTabSelected(tab)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )


        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(state.items, key = { it.id }) { org ->
                OrganizationCard(
                    name = org.name,
                    imageUrl = org.coverUrl,
                    onClick = { navigator.navigate("organizations/${org.id}") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                when {
                    state.isPaging -> {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    state.error != null -> {
                        Text(
                            "Ошибка: ${state.error}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}