package ru.truebusiness.liveposter_android_client.view.organizationslist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.truebusiness.liveposter_android_client.view.components.FilterChipsSection
import ru.truebusiness.liveposter_android_client.view.organizationslist.components.TopBarSection

enum class OrganizationFilter { All, Subscriptions, MyOrganizations }

@Composable
fun OrganizationsPage(
    state: OrganizationsUiState = remember { OrganizationsUiState() },
    onBack: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onSearchChange: (String) -> Unit = {},
    onFilterSelected: (OrganizationFilter) -> Unit = {}
) {
    val gradient = Brush.verticalGradient(
        0f to Color(0xFFF5B841),
//        0.35f to Color(0xFFFFC98A),
        1f to Color.White
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        TopBarSection(
            searchText = state.searchText,
            onSearchChange = onSearchChange,
            onBackClick = onBack,
            onFilterClick = onFilterClick
        )
        Spacer(Modifier.height(12.dp))
        FilterChipsSection(
            selected = state.selectedFilter,
            onSelected = onFilterSelected
        )

        Spacer(Modifier.height(12.dp))
    }
}