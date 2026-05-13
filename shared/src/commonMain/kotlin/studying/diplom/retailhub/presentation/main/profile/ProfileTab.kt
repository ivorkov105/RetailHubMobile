package studying.diplom.retailhub.presentation.main.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import studying.diplom.retailhub.presentation.main.components.AnalyticsDashboardSection
import studying.diplom.retailhub.presentation.main.components.DashboardButton
import studying.diplom.retailhub.presentation.main.components.DashboardItem
import studying.diplom.retailhub.presentation.main.components.MyShiftsSection
import studying.diplom.retailhub.presentation.main.components.PeriodSelector
import studying.diplom.retailhub.presentation.main.departments.department.DepartmentScreen
import studying.diplom.retailhub.presentation.main.employees.employee.EmployeeScreen
import studying.diplom.retailhub.presentation.main.qr.create_qr.CreateQrScreen
import studying.diplom.retailhub.presentation.main.qr.qr_list.QrListScreen
import studying.diplom.retailhub.presentation.main.store.create_store.CreateStoreScreen
import studying.diplom.retailhub.presentation.main.store.my_store.MyStoreScreen
import studying.diplom.retailhub.presentation.main.utils.ScreenMode
import studying.diplom.retailhub.presentation.main.utils.UserRoles
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
					index = 3u,
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
					is ProfileNavigationEvent.NavigateToAuth             -> {
						navigator.parent?.replace(AuthScreen())
					}

					is ProfileNavigationEvent.NavigateToMyStore          -> {
						navigator.parent?.push(MyStoreScreen())
					}

					is ProfileNavigationEvent.NavigateToCreateStore      -> {
						navigator.parent?.push(CreateStoreScreen())
					}

					is ProfileNavigationEvent.NavigateToUpdateStore      -> {
						navigator.parent?.push(CreateStoreScreen(initialStore = event.store))
					}

					is ProfileNavigationEvent.NavigateToCreateDepartment -> {
						navigator.parent?.push(DepartmentScreen(mode = ScreenMode.CREATE))
					}

					is ProfileNavigationEvent.NavigateToCreateEmployee   -> {
						navigator.parent?.push(EmployeeScreen(mode = ScreenMode.CREATE))
					}

					is ProfileNavigationEvent.NavigateToCreateQr         -> {
						navigator.parent?.push(CreateQrScreen())
					}

					ProfileNavigationEvent.NavigateToQrList              -> {
						navigator.parent?.push(QrListScreen())
					}
				}
			}
		}

		if (state.isLoading) {
			Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
				CircularProgressIndicator()
			}
		} else {
			LazyVerticalGrid(
				columns = GridCells.Fixed(2),
				horizontalArrangement = Arrangement.spacedBy(12.dp),
				verticalArrangement = Arrangement.spacedBy(12.dp),
				contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp),
				modifier = Modifier.fillMaxSize()
			) {
				state.error?.let { error ->
					item(span = { GridItemSpan(2) }) {
						Surface(
							color = MaterialTheme.colorScheme.errorContainer,
							shape = RoundedCornerShape(8.dp),
							modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
						) {
							Text(
								text = error,
								color = MaterialTheme.colorScheme.onErrorContainer,
								modifier = Modifier.padding(8.dp),
								style = MaterialTheme.typography.bodySmall
							)
						}
					}
				}

				item(span = { GridItemSpan(2) }) {
					Column(horizontalAlignment = Alignment.CenterHorizontally) {
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

				state.user?.departments?.let { departments ->
					if (departments.isNotEmpty()) {
						item(span = { GridItemSpan(2) }) {
							Column {
								Spacer(modifier = Modifier.height(16.dp))
								Text(
									text = "Мои отделы",
									style = MaterialTheme.typography.titleMedium,
									fontWeight = FontWeight.Bold
								)
								Spacer(modifier = Modifier.height(8.dp))
							}
						}
						items(departments, span = { GridItemSpan(2) }) { department ->
							Surface(
								modifier = Modifier
									.fillMaxWidth()
									.padding(vertical = 4.dp),
								color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
								shape = RoundedCornerShape(8.dp)
							) {
								Text(
									text = department.name,
									modifier = Modifier.padding(12.dp),
									style = MaterialTheme.typography.bodyLarge
								)
							}
						}
					}
				}

				if (state.user?.role?.uppercase() == UserRoles.MANAGER.name) {
					item(span = { GridItemSpan(2) }) {
						Column {
							Text(
								text = "Аналитика",
								style = MaterialTheme.typography.titleMedium,
								fontWeight = FontWeight.Bold,
								modifier = Modifier.padding(bottom = 12.dp)
							)
						}
					}

					state.dashboard?.let { dashboard ->
						item(span = { GridItemSpan(2) }) {
							AnalyticsDashboardSection(
								dashboard = dashboard,
								selectedPeriod = state.selectedPeriod,
								onPeriodSelected = { screenModel.onEvent(ProfileEvent.OnPeriodChange(it)) },
								modifier = Modifier.padding(top = 16.dp)
							)
						}
					}

					item(span = { GridItemSpan(2) }) {
						Text(
							text = "Управление",
							style = MaterialTheme.typography.titleMedium,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(top = 16.dp)
						)
					}

					val dashboardItems = listOf(
						DashboardItem("Создать магазин", ProfileEvent.OnCreateStoreClick),
						DashboardItem("Обновить магазин", ProfileEvent.OnUpdateStoreClick),
						DashboardItem("Создать отдел", ProfileEvent.OnCreateDepartmentClick),
						DashboardItem("Создать сотрудника", ProfileEvent.OnCreateEmployeeClick),
						DashboardItem("Создать QR", ProfileEvent.OnQrClick),
						DashboardItem("QR-коды", ProfileEvent.OnQrListClick),
					)

					items(dashboardItems) { item ->
						DashboardButton(item) { screenModel.onEvent(item.event) }
					}
				} else if (state.user?.role?.uppercase() == UserRoles.CONSULTANT.name) {
					item(span = { GridItemSpan(2) }) {
						MyShiftsSection(
							shifts = state.shifts,
							isShiftsLoading = state.isShiftsLoading,
							dateFrom = state.dateFrom,
							dateTo = state.dateTo,
							onDateFromChange = { screenModel.onEvent(ProfileEvent.OnDateFromChange(it)) },
							onDateToChange = { screenModel.onEvent(ProfileEvent.OnDateToChange(it)) },
							currentStatus = state.user?.currentStatus ?: "OFFLINE",
							onStartShiftClick = { screenModel.onEvent(ProfileEvent.OnStartShiftClick) },
							onEndShiftClick = { screenModel.onEvent(ProfileEvent.OnEndShiftClick) }
						)
					}
				}

				item(span = { GridItemSpan(2) }) {
					Spacer(modifier = Modifier.height(16.dp))
					Surface(
						modifier = Modifier
							.fillMaxWidth()
							.height(50.dp)
							.clickable { screenModel.onEvent(ProfileEvent.Logout) },
						color = Color.Transparent,
						border = BorderStroke(1.dp, Color.Red),
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
