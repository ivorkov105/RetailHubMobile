package studying.diplom.retailhub.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf
import studying.diplom.retailhub.presentation.main.departments.department_list.DepartmentsListTab
import studying.diplom.retailhub.presentation.main.employees.employees_list.EmployeesTab
import studying.diplom.retailhub.presentation.main.profile.ProfileTab
import studying.diplom.retailhub.presentation.main.requests.RequestsTab
import studying.diplom.retailhub.presentation.main.utils.UserRoles
import studying.diplom.retailhub.presentation.notifications.NotificationsScreen
import studying.diplom.retailhub.resources.Res
import studying.diplom.retailhub.resources.ic_bell

class MainScreen(private val userRole: String? = null) : Screen {

	@OptIn(ExperimentalMaterial3Api::class)
	@Composable
	override fun Content() {
		val screenModel: MainViewModel = koinScreenModel { parametersOf(userRole) }
		val state by screenModel.state.collectAsState()
		val navigator = LocalNavigator.currentOrThrow

		DisposableEffect(Unit) {
			onDispose {
				screenModel.onEvent(MainEvent.OnCloseApp)
			}
		}

		val initialTab = remember(userRole) {
			if (userRole == UserRoles.CONSULTANT.name) RequestsTab else EmployeesTab
		}

		TabNavigator(initialTab) { tabNavigator ->
			Scaffold(
				topBar = {
					TopAppBar(
						title = { Text(tabNavigator.current.options.title) },
						actions = {
							Box(modifier = Modifier.padding(end = 8.dp)) {
								IconButton(
									onClick = {
										navigator.push(NotificationsScreen()
										)
									}
								) {
									Icon(
										painter = painterResource(Res.drawable.ic_bell),
										contentDescription = "Уведомления",
										modifier = Modifier.size(24.dp)
									)
								}
								if (state.hasUnreadNotifications) {
									Box(
										modifier = Modifier
											.align(Alignment.BottomEnd)
											.padding(
												bottom = 8.dp,
												end = 8.dp
											)
											.size(12.dp)
											.background(
												Color(0xFF00A3FF),
												CircleShape
											)
									)
								}
							}
						}
					)
				},
				bottomBar = {
					Column {
						HorizontalDivider(
							thickness = 1.dp,
							color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
						)
						NavigationBar(
							tonalElevation = 0.dp,
							containerColor = MaterialTheme.colorScheme.surface
						) {
							if (userRole == UserRoles.MANAGER.name) {
								TabNavigationItem(EmployeesTab, state, screenModel)
								TabNavigationItem(DepartmentsListTab, state, screenModel)
							} else {
								TabNavigationItem(RequestsTab, state, screenModel)
							}
							TabNavigationItem(ProfileTab, state, screenModel)
						}
					}
				}
			) { paddingValues ->
				if (tabNavigator.current != state.currentTab) {
					tabNavigator.current = state.currentTab
				}

				Box(modifier = Modifier.padding(paddingValues)) {
					CurrentTab()
				}
			}
		}
	}
}

@Composable
private fun RowScope.TabNavigationItem(
	tab: Tab,
	state: MainState,
	screenModel: MainViewModel
) {
	NavigationBarItem(
		selected = state.currentTab == tab,
		onClick = { screenModel.onEvent(MainEvent.SelectTab(tab)) },
		icon = {
			tab.options.icon?.let { icon ->
				Icon(
					modifier = Modifier.size(24.dp),
					painter = icon,
					contentDescription = tab.options.title,
					tint = if (state.currentTab == tab)
						MaterialTheme.colorScheme.primary
					else Color.Gray,
				)
			}
		},
		label = {
			Text(
				text = tab.options.title,
				color = if (state.currentTab == tab)
					MaterialTheme.colorScheme.primary
				else Color.Gray
			)
		},
		colors = NavigationBarItemDefaults.colors(
			indicatorColor = MaterialTheme.colorScheme.surfaceVariant
		)
	)
}
