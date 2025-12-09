package ru.truebusiness.liveposter_android_client.view.event

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.truebusiness.liveposter_android_client.data.Event
import ru.truebusiness.liveposter_android_client.data.User
import ru.truebusiness.liveposter_android_client.ui.theme.EventPageTopFooterColor
import ru.truebusiness.liveposter_android_client.ui.theme.pageGradient
import ru.truebusiness.liveposter_android_client.view.event.components.EventActionsCard
import ru.truebusiness.liveposter_android_client.view.event.components.EventAdditionalInfoCard
import ru.truebusiness.liveposter_android_client.view.event.components.EventMainInfoCard
import ru.truebusiness.liveposter_android_client.view.event.components.EventPostsFeed
import ru.truebusiness.liveposter_android_client.view.viewmodel.EventDetailsEvent
import ru.truebusiness.liveposter_android_client.view.viewmodel.EventDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsPage(
    viewModel: EventDetailsViewModel,
    onBack: () -> Unit,
    onShareClick: (String) -> Unit = {},
    onShowParticipants: (List<User>) -> Unit = {},
    onEditClick: (Event) -> Unit = {},
    onCancelEventClick: (Event) -> Unit = {},
    onOrganizerClick: (String) -> Unit = {},
    onShowError: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is EventDetailsEvent.Error -> onShowError(event.message)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.mainInfo.title.ifBlank { "" },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EventPageTopFooterColor
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(pageGradient)
        ) {
            uiState.event?.let {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    EventMainInfoCard(uiState.mainInfo)

                    EventActionsCard(
                        state = uiState.actions,
                        onPrimaryAction = viewModel::onPrimaryAction,
                        onShareClick = {
                            uiState.actions.shareLink?.let(onShareClick)
                        },
                        onShowParticipants = {
                            if (uiState.actions.participants.isNotEmpty()) {
                                onShowParticipants(uiState.actions.participants)
                            }
                        },
                        onEditClick = {
                            uiState.event?.let(onEditClick)
                        },
                        onCancelEventClick = {
                            uiState.event?.let(onCancelEventClick)
                        }
                    )

                    if (uiState.additionalInfo.isVisible) {
                        EventAdditionalInfoCard(
                            state = uiState.additionalInfo,
                            onOrganizerClick = onOrganizerClick
                        )
                    }

                    if (uiState.posts.isNotEmpty()) {
                        EventPostsFeed(posts = uiState.posts)
                    }
                }
            }

            if (uiState.isLoading && uiState.event == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            if (!uiState.isLoading && uiState.event == null && uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage ?: "Неизвестная ошибка",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
