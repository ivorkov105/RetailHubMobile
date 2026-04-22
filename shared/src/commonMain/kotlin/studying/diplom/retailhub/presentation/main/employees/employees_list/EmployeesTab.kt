package studying.diplom.retailhub.presentation.main.employees.employees_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import studying.diplom.retailhub.presentation.main.components.BaseListItem
import studying.diplom.retailhub.presentation.main.employees.employee.EmployeeScreen
import studying.diplom.retailhub.presentation.main.utils.ScreenMode
import studying.diplom.retailhub.resources.Res
import studying.diplom.retailhub.resources.ic_list

object EmployeesTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Сотрудники"
            val icon = painterResource(Res.drawable.ic_list)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val screenModel: EmployeesListViewModel = koinScreenModel()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            screenModel.onEvent(EmployeesListEvent.LoadList)

            screenModel.navigationEvents.collectLatest { event ->
                when (event) {
                    is EmployeesListNavigationEvent.NavigateToEmployee -> {
                        navigator.parent?.push(EmployeeScreen(event.employee, mode = ScreenMode.SHOW))
                    }
                }
            }
        }

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            if (state.employees.isNotEmpty()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 0.dp)
                    ) {
                        items(state.employees) { employee ->
                            val statusColor = when (employee.currentStatus) {
                                "ACTIVE" -> Color(0xFF4CAF50)
                                "BUSY" -> Color(0xFFF44336)
                                else -> Color.Gray
                            }
                            BaseListItem(
                                title = "${employee.firstName} ${employee.lastName}",
                                statusText = employee.currentStatus,
                                statusColor = statusColor,
                                onClick = { screenModel.onEvent(EmployeesListEvent.OnEmployeeClick(employee)) }
                            )
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Нет сотрудников",
                        textAlign = TextAlign.Center,
						color = Color.Gray
                    )
                }
            }
        }
    }
}
