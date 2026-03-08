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
import studying.diplom.retailhub.presentation.main.components.DashboardButton
import studying.diplom.retailhub.presentation.main.components.DashboardItem
import studying.diplom.retailhub.presentation.main.store.create_store.CreateStoreScreen
import studying.diplom.retailhub.presentation.main.departments.department.DepartmentScreen
import studying.diplom.retailhub.presentation.main.employees.employee.EmployeeScreen
import studying.diplom.retailhub.presentation.main.store.my_store.MyStoreScreen
import studying.diplom.retailhub.presentation.main.utils.ScreenMode
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
	                is ProfileNavigationEvent.NavigateToCreateDepartment -> {
						navigator.parent?.push(DepartmentScreen(mode = ScreenMode.CREATE))
					}
	                is ProfileNavigationEvent.NavigateToCreateEmployee -> {
	                    navigator.parent?.push(EmployeeScreen(mode = ScreenMode.CREATE))
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
                        DashboardItem("Создать магазин", ProfileEvent.OnCreateStoreClick),
                        DashboardItem("Обновить магазин", ProfileEvent.OnUpdateStoreClick),
                        DashboardItem("Создать отдел", ProfileEvent.OnCreateDepartmentClick),
						DashboardItem("Создать консультанта", ProfileEvent.OnCreateEmployeeClick),
						//DashboardItem("QR", ProfileEvent.OnQrClick),
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