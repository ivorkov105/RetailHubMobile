package studying.diplom.retailhub.presentation.main.departments.department

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf
import studying.diplom.retailhub.domain.models.request.RequestStatus
import studying.diplom.retailhub.domain.models.shop.DepartmentModel
import studying.diplom.retailhub.presentation.auth.AuthScreen
import studying.diplom.retailhub.presentation.main.components.DeleteConfirmDialog
import studying.diplom.retailhub.presentation.main.components.InfoBlock
import studying.diplom.retailhub.presentation.main.components.RequestsFilterDialog
import studying.diplom.retailhub.presentation.main.requests.components.RequestsListItem
import studying.diplom.retailhub.presentation.main.utils.ScreenMode
import studying.diplom.retailhub.resources.Res
import studying.diplom.retailhub.resources.ic_arrow_left
import studying.diplom.retailhub.resources.ic_edit
import studying.diplom.retailhub.resources.ic_filter_alt_on
import studying.diplom.retailhub.resources.ic_trash_can

class DepartmentScreen(
	private val initialDepartment: DepartmentModel? = null,
	private val mode: ScreenMode = ScreenMode.SHOW
) : Screen {

	@OptIn(ExperimentalMaterial3Api::class)
	@Composable
	override fun Content() {
		val navigator = LocalNavigator.currentOrThrow
		val screenModel: DepartmentViewModel = koinScreenModel { parametersOf(initialDepartment) }
		val state by screenModel.state.collectAsState()
		var currentMode by remember { mutableStateOf(mode) }
		var showDeleteDialog by remember { mutableStateOf(false) }

		LaunchedEffect(Unit) {
			if (currentMode != ScreenMode.CREATE) screenModel.onEvent(DepartmentEvent.OnLoadDepartment)

			screenModel.navigationEvents.collectLatest { event ->
				when (event) {
					DepartmentNavigationEvent.NavigateBack -> navigator.pop()
					DepartmentNavigationEvent.NavigateToAuth -> navigator.parent?.replace(AuthScreen())
				}
			}
		}

		if (showDeleteDialog) {
			DeleteConfirmDialog(
				title = "Удалить отдел?",
				text = "Вы уверены, что хотите удалить этот отдел? Это действие нельзя будет отменить.",
				onDismiss = { showDeleteDialog = false },
				onConfirm = {
					showDeleteDialog = false
					screenModel.onEvent(DepartmentEvent.OnDeleteClick)
				}
			)
		}

		Scaffold(
			topBar = {
				TopAppBar(
					title = {
						Text(
							when (currentMode) {
								ScreenMode.CREATE -> "Создание отдела"
								ScreenMode.UPDATE -> "Редактирование отдела"
								else              -> "Просмотр отдела"
							}
						)
					},
					navigationIcon = {
						IconButton(onClick = { screenModel.onEvent(DepartmentEvent.OnBackClick) }) {
							Icon(
								painter = painterResource(Res.drawable.ic_arrow_left),
								contentDescription = "Назад",
								modifier = Modifier.size(24.dp)
							)
						}
					},
					actions = {
						if (currentMode == ScreenMode.SHOW) {
							IconButton(onClick = { currentMode = ScreenMode.UPDATE }) {
								Icon(
									painter = painterResource(Res.drawable.ic_edit),
									contentDescription = "Редактировать",
									modifier = Modifier.size(24.dp)
								)
							}
						} else if (currentMode == ScreenMode.UPDATE) {
							IconButton(
								onClick = {
									showDeleteDialog = true
								}
							) {
								Icon(
									painterResource(Res.drawable.ic_trash_can),
									contentDescription = "Удалить",
									modifier = Modifier.size(24.dp),
								)
							}
						}
					}
				)
			}
		) { paddingValues ->
			if (state.isLoading) {
				Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
					CircularProgressIndicator()
				}
			} else {
				Column(
					modifier = Modifier
						.fillMaxSize()
						.padding(paddingValues)
						.padding(horizontal = 16.dp)
				) {
					Column(
						modifier = Modifier.padding(vertical = 16.dp),
						verticalArrangement = Arrangement.spacedBy(24.dp)
					) {
						InfoBlock(
							label = "Название отдела",
							value = state.data?.name ?: "",
							mode = currentMode,
							onValueChange = { screenModel.onEvent(DepartmentEvent.OnNameChange(it)) }
						)

						InfoBlock(
							label = "Описание",
							value = state.data?.description ?: "",
							mode = currentMode,
							onValueChange = { screenModel.onEvent(DepartmentEvent.OnDescriptionChange(it)) }
						)
					}

					if (currentMode == ScreenMode.SHOW) {
						Spacer(modifier = Modifier.height(8.dp))

						Row(
							modifier = Modifier.fillMaxWidth(),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.CenterVertically
						) {
							Text(
								text = "Заявки отдела",
								style = MaterialTheme.typography.titleLarge
							)

							OutlinedButton(
								onClick = { screenModel.onEvent(DepartmentEvent.OnToggleFilterDialog) },
								contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
							) {
								Icon(
									painter = painterResource(Res.drawable.ic_filter_alt_on),
									contentDescription = null,
									modifier = Modifier.size(18.dp)
								)
								Spacer(modifier = Modifier.width(8.dp))
								Text("Фильтры")
							}
						}

						Spacer(modifier = Modifier.height(16.dp))

						Box(modifier = Modifier.weight(1f)) {
							if (state.isRequestsLoading && state.requests.isEmpty()) {
								CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
							} else if (state.requests.isEmpty() && !state.isRequestsLoading) {
								Text(
									text = "Заявок в этом отделе пока нет",
									modifier = Modifier
										.width(12.dp),
									textAlign = TextAlign.Center,
									style = MaterialTheme.typography.bodyMedium,
									color = MaterialTheme.colorScheme.onSurfaceVariant
								)
							} else {
								LazyColumn(
									modifier = Modifier.fillMaxSize(),
									verticalArrangement = Arrangement.spacedBy(8.dp)
								) {
									items(state.requests, key = { it.id }) { request ->
										RequestsListItem(
											request = request,
											currentUserId = state.currentUserId,
											onButtonClick = {
												val isAssignedToMe = request.assignedUserId != null &&
													request.assignedUserId == state.currentUserId

												if (request.status == RequestStatus.ASSIGNED || (request.status == RequestStatus.ESCALATED && isAssignedToMe)) {
													screenModel.onEvent(DepartmentEvent.OnShowCompleteDialog(request))
												} else {
													screenModel.onEvent(DepartmentEvent.OnShowAcceptDialog(request))
												}
											}
										)
									}
									item { Spacer(modifier = Modifier.height(16.dp)) }
								}
							}
						}
					}

					if (currentMode == ScreenMode.CREATE || currentMode == ScreenMode.UPDATE) {
						Spacer(modifier = Modifier.weight(1f))
						Button(
							onClick = {
								if (currentMode == ScreenMode.CREATE) screenModel.onEvent(DepartmentEvent.OnSaveClick)
								else screenModel.onEvent(DepartmentEvent.OnUpdateClick)
							},
							modifier = Modifier
								.fillMaxWidth()
								.padding(vertical = 16.dp)
								.height(50.dp),
							shape = RoundedCornerShape(8.dp)
						) {
							Text("Сохранить")
						}
					}
				}
			}

			if (state.showFilterDialog) {
				RequestsFilterDialog(
					selectedStatus = state.filterStatus,
					onStatusChange = { screenModel.onEvent(DepartmentEvent.OnFilterStatusChange(it)) },
					dateFrom = state.filterDateFrom,
					onDateFromChange = { screenModel.onEvent(DepartmentEvent.OnFilterDateFromChange(it)) },
					dateTo = state.filterDateTo,
					onDateToChange = { screenModel.onEvent(DepartmentEvent.OnFilterDateToChange(it)) },
					onDismiss = { screenModel.onEvent(DepartmentEvent.OnToggleFilterDialog) },
					onApply = { screenModel.onEvent(DepartmentEvent.OnApplyFilters) },
					onClear = { screenModel.onEvent(DepartmentEvent.OnClearFilters) }
				)
			}

			state.requestToAccept?.let { request ->
				AlertDialog(
					onDismissRequest = { screenModel.onEvent(DepartmentEvent.OnDismissAcceptDialog) },
					title = { Text(text = "Взять заявку?") },
					text = { Text(text = "Вы уверены, что хотите взять в работу заявку из этого отдела?") },
					confirmButton = {
						TextButton(onClick = { screenModel.onEvent(DepartmentEvent.OnAcceptRequest(request.id)) }) {
							Text("Да")
						}
					},
					dismissButton = {
						TextButton(onClick = { screenModel.onEvent(DepartmentEvent.OnDismissAcceptDialog) }) {
							Text("Нет")
						}
					}
				)
			}

			state.requestToComplete?.let { request ->
				AlertDialog(
					onDismissRequest = { screenModel.onEvent(DepartmentEvent.OnDismissCompleteDialog) },
					title = { Text(text = "Завершить заявку?") },
					text = { Text(text = "Вы уверены, что хотите завершить работу над заявкой?") },
					confirmButton = {
						TextButton(onClick = { screenModel.onEvent(DepartmentEvent.OnCompleteRequest(request.id)) }) {
							Text("Да")
						}
					},
					dismissButton = {
						TextButton(onClick = { screenModel.onEvent(DepartmentEvent.OnDismissCompleteDialog) }) {
							Text("Нет")
						}
					}
				)
			}

			if (state.showStartShiftDialog) {
				AlertDialog(
					onDismissRequest = { screenModel.onEvent(DepartmentEvent.OnDismissStartShiftDialog) },
					title = { Text(text = "Смена не начата") },
					text = { Text(text = "Чтобы принимать заявки, необходимо начать смену. Начать сейчас?") },
					confirmButton = {
						TextButton(onClick = { screenModel.onEvent(DepartmentEvent.OnConfirmStartShift) }) {
							Text("Да")
						}
					},
					dismissButton = {
						TextButton(onClick = { screenModel.onEvent(DepartmentEvent.OnDismissStartShiftDialog) }) {
							Text("Нет")
						}
					}
				)
			}

			state.error?.let { error ->
				AlertDialog(
					onDismissRequest = { screenModel.onEvent(DepartmentEvent.OnDismissErrorDialog) },
					title = { Text(text = "Ошибка") },
					text = { Text(text = error) },
					confirmButton = {
						TextButton(onClick = { screenModel.onEvent(DepartmentEvent.OnDismissErrorDialog) }) {
							Text("ОК")
						}
					}
				)
			}
		}
	}
}
