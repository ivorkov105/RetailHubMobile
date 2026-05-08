package studying.diplom.retailhub.presentation.main.requests

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.painterResource
import studying.diplom.retailhub.domain.models.request.RequestStatus
import studying.diplom.retailhub.presentation.main.components.RequestsFilterDialog
import studying.diplom.retailhub.presentation.main.requests.components.RequestsListItem
import studying.diplom.retailhub.resources.Res
import studying.diplom.retailhub.resources.ic_list

object RequestsTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Заявки"
            val icon = painterResource(Res.drawable.ic_list)
            return remember { TabOptions(index = 2u, title = title, icon = icon) }
        }

    @Composable
    override fun Content() {
        val screenModel: RequestsViewModel = koinScreenModel()
        val state by screenModel.state.collectAsState()
        val listState = rememberLazyListState()

        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isLoading && state.requests.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.requests.isEmpty() && !state.isLoading) {
                Text(
                    text = "Заявок не найдено",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
                ) {
                    item { Box(modifier = Modifier.padding(top = 16.dp)) }

                    items(state.requests, key = { it.id }) { request ->
                        RequestsListItem(
                            request = request,
                            currentUserId = state.currentUserId,
                            onButtonClick = { 
                                val isAssignedToMe = request.assignedUserId != null &&
                                    request.assignedUserId == state.currentUserId

                                if (request.status == RequestStatus.ASSIGNED || (request.status == RequestStatus.ESCALATED && isAssignedToMe)) {
                                    screenModel.onEvent(RequestsEvent.OnShowCompleteDialog(request))
                                } else {
                                    screenModel.onEvent(RequestsEvent.OnShowAcceptDialog(request))
                                }
                            },
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    item { Box(modifier = Modifier.padding(bottom = 16.dp)) }
                }
                
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            if (state.showFilterDialog) {
                RequestsFilterDialog(
                    selectedStatus = state.filterStatus,
                    onStatusChange = { screenModel.onEvent(RequestsEvent.OnFilterStatusChange(it)) },
                    dateFrom = state.filterDateFrom,
                    onDateFromChange = { screenModel.onEvent(RequestsEvent.OnFilterDateFromChange(it)) },
                    dateTo = state.filterDateTo,
                    onDateToChange = { screenModel.onEvent(RequestsEvent.OnFilterDateToChange(it)) },
                    onDismiss = { screenModel.onEvent(RequestsEvent.OnToggleFilterDialog) },
                    onApply = { screenModel.onEvent(RequestsEvent.OnApplyFilters) },
                    onClear = { screenModel.onEvent(RequestsEvent.OnClearFilters) }
                )
            }

            state.requestToAccept?.let { request ->
                AlertDialog(
                    onDismissRequest = { screenModel.onEvent(RequestsEvent.OnDismissAcceptDialog) },
                    title = { Text(text = "Взять заявку?") },
                    text = { Text(text = "Вы уверены, что хотите взять в работу заявку из отдела ${request.departmentName}?") },
                    confirmButton = {
                        TextButton(onClick = { screenModel.onEvent(RequestsEvent.OnAcceptRequest(request.id)) }) {
                            Text("Да")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { screenModel.onEvent(RequestsEvent.OnDismissAcceptDialog) }) {
                            Text("Нет")
                        }
                    }
                )
            }

            state.requestToComplete?.let { request ->
                AlertDialog(
                    onDismissRequest = { screenModel.onEvent(RequestsEvent.OnDismissCompleteDialog) },
                    title = { Text(text = "Завершить заявку?") },
                    text = { Text(text = "Вы уверены, что хотите завершить работу над заявкой из отдела ${request.departmentName}?") },
                    confirmButton = {
                        TextButton(onClick = { screenModel.onEvent(RequestsEvent.OnCompleteRequest(request.id)) }) {
                            Text("Да")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { screenModel.onEvent(RequestsEvent.OnDismissCompleteDialog) }) {
                            Text("Нет")
                        }
                    }
                )
            }

            if (state.showStartShiftDialog) {
                AlertDialog(
                    onDismissRequest = { screenModel.onEvent(RequestsEvent.OnDismissStartShiftDialog) },
                    title = { Text(text = "Смена не начата") },
                    text = { Text(text = "Чтобы принимать заявки, необходимо начать смену. Начать сейчас?") },
                    confirmButton = {
                        TextButton(onClick = { screenModel.onEvent(RequestsEvent.OnConfirmStartShift) }) {
                            Text("Да")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { screenModel.onEvent(RequestsEvent.OnDismissStartShiftDialog) }) {
                            Text("Нет")
                        }
                    }
                )
            }

            state.error?.let { error ->
                AlertDialog(
                    onDismissRequest = { screenModel.onEvent(RequestsEvent.OnDismissErrorDialog) },
                    title = { Text(text = "Ошибка") },
                    text = { Text(text = error) },
                    confirmButton = {
                        TextButton(onClick = { screenModel.onEvent(RequestsEvent.OnRetryLoad) }) {
                            Text("Повторить")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { screenModel.onEvent(RequestsEvent.OnDismissErrorDialog) }) {
                            Text("Закрыть")
                        }
                    }
                )
            }
        }
    }
}
