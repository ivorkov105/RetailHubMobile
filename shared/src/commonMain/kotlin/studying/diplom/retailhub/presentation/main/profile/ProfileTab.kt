package studying.diplom.retailhub.presentation.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import studying.diplom.retailhub.presentation.auth.AuthScreen
import studying.diplom.retailhub.presentation.main.store.create_store.CreateStoreScreen
import studying.diplom.retailhub.presentation.main.store.my_store.MyStoreScreen
import studying.diplom.retailhub.resources.Res
import studying.diplom.retailhub.resources.ic_profile

object ProfileTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Профиль"
            val icon = painterResource(Res.drawable.ic_profile)

            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val screenModel: ProfileViewModel = koinScreenModel()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            screenModel.onEvent(ProfileEvent.LoadProfile)
            
            screenModel.navigationEvents.collectLatest { event ->
                when (event) {
                    is ProfileNavigationEvent.NavigateToAuth -> {
                        navigator.parent?.replace(AuthScreen())
                    }
                    is ProfileNavigationEvent.NavigateToMyStore -> {
                        navigator.parent?.push(MyStoreScreen())
                    }
                    is ProfileNavigationEvent.NavigateToCreateStore -> {
                        navigator.parent?.push(CreateStoreScreen())
                    }
	                is ProfileNavigationEvent.NavigateToUpdateStore -> {
		                navigator.parent?.push(CreateStoreScreen(initialStore = event.store))
	                }
                }
            }
        }

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item(span = { GridItemSpan(2) }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Box(
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray)
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp),
                                color = Color(0xFF00A3FF),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${state.user?.firstName ?: ""} ${state.user?.lastName ?: ""}".ifBlank { "Пользователь" },
                                        color = Color.White,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            
                            state.store?.let { store ->
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = store.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = store.address,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }

                    val dashboardItems = listOf(
                        DashboardItem("Обновить отдел", Color(0xFFFFA500), "PUT", ProfileEvent.LoadProfile),
                        DashboardItem("Удалить отдел", Color(0xFFFF4B4B), "DELETE", ProfileEvent.LoadProfile),
                        DashboardItem("Создать магазин", Color(0xFF4CAF50), "POST", ProfileEvent.OnCreateStoreClick),
                        DashboardItem("Мой магазин", Color(0xFF2196F3), "GET", ProfileEvent.OnMyStoreClick),
                        DashboardItem("Обновить магазин", Color(0xFFFFA500), "PUT", ProfileEvent.OnUpdateStoreClick),
                        DashboardItem("Список отделов", Color(0xFF2196F3), "GET", ProfileEvent.LoadProfile),
                        DashboardItem("Создать отдел", Color(0xFF4CAF50), "POST", ProfileEvent.LoadProfile)
                    )

                    items(dashboardItems) { item ->
                        DashboardButton(item) { screenModel.onEvent(item.event) }
                    }
                    
                    item(span = { GridItemSpan(2) }) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clickable { screenModel.onEvent(ProfileEvent.Logout) },
                            color = Color.Transparent,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("Выйти", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardButton(
    item: DashboardItem,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick() },
        color = Color.White,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                color = item.color,
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = item.method,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            Text(
                text = item.label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray,
                lineHeight = 16.sp
            )
        }
    }
}

data class DashboardItem(
    val label: String,
    val color: Color,
    val method: String,
    val event: ProfileEvent
)
