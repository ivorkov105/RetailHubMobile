package studying.diplom.retailhub.presentation.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.painterResource
import studying.diplom.retailhub.presentation.components.NotificationsListItem
import studying.diplom.retailhub.resources.Res
import studying.diplom.retailhub.resources.ic_arrow_left

class NotificationsScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel: NotificationsViewModel = koinScreenModel()
        val state by screenModel.state.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Уведомления") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_arrow_left),
                                contentDescription = "Назад",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                if (state.isLoading && state.notifications.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (state.notifications.isEmpty()) {
                    Text(
                        text = "У вас пока нет уведомлений",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.notifications) { notification ->
                            NotificationsListItem(
                                notification = notification,
                                isExpanded = state.expandedNotificationId == notification.id,
                                onClick = { 
                                    screenModel.onEvent(NotificationsEvent.ToggleExpand(notification.id))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
