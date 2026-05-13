package studying.diplom.retailhub.presentation.main.requests

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.painterResource
import studying.diplom.retailhub.domain.models.request.RequestModel
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
        val pagedRequests = screenModel.requestsPagingData.collectAsLazyPagingItems()
        val listState = rememberLazyListState()

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                pagedRequests.loadState.refresh is LoadState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                pagedRequests.itemCount == 0 && pagedRequests.loadState.refresh is LoadState.NotLoading -> {
                    Text(
                        text = "Заявок не найдено",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                pagedRequests.loadState.refresh is LoadState.Error -> {
                    val errorState = pagedRequests.loadState.refresh as LoadState.Error
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = errorState.error.message ?: "Ошибка загрузки")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { pagedRequests.retry() }) {
                            Text("Повторить")
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
                    ) {
                        item { Box(modifier = Modifier.padding(top = 16.dp)) }

                        items(pagedRequests.itemCount) { index ->
                            val request: RequestModel? = pagedRequests[index]
                            request?.let { req ->
                                RequestsListItem(
                                    request = req,
                                    currentUserId = state.currentUserId,
                                    onButtonClick = { 
                                        val isAssignedToMe = req.assignedUserId != null &&
                                            req.assignedUserId == state.currentUserId

                                        if (req.status == RequestStatus.ASSIGNED || (req.status == RequestStatus.ESCALATED && isAssignedToMe)) {
                                            screenModel.onEvent(RequestsEvent.OnShowCompleteDialog(req))
                                        } else {
                                            screenModel.onEvent(RequestsEvent.OnShowAcceptDialog(req))
                                        }
                                    },
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }

                        if (pagedRequests.loadState.append is LoadState.Loading) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        if (pagedRequests.loadState.append is LoadState.Error) {
                            item {
                                val errorState = pagedRequests.loadState.append as LoadState.Error
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = errorState.error.message ?: "Ошибка загрузки")
                                    Button(onClick = { pagedRequests.retry() }) {
                                        Text("Повторить")
                                    }
                                }
                            }
                        }

                        item { Box(modifier = Modifier.padding(bottom = 16.dp)) }
                    }
                }
            }

            if (state.isLoading) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
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

            state.error?.let { error ->
                AlertDialog(
                    onDismissRequest = { screenModel.onEvent(RequestsEvent.OnDismissErrorDialog) },
                    title = { Text(text = "Ошибка") },
                    text = { Text(text = error) },
                    confirmButton = {
                        TextButton(onClick = { screenModel.onEvent(RequestsEvent.OnDismissErrorDialog) }) {
                            Text("Ок")
                        }
                    }
                )
            }
        }
    }
}
