package studying.diplom.retailhub.presentation.main.employees

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.painterResource
import studying.diplom.retailhub.presentation.main.components.BaseListItem
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
        val employees = listOf(
            EmployeeStub("Воронков Илья Игоревич", "В работе", Color(0xFF00FF00)),
            EmployeeStub("Дранкевич Матвей Евгеньевич", "Неактивен", Color.Gray),
            EmployeeStub("Зяблицкий Фёдор Андреевич", "Уволен", Color.Red),
            EmployeeStub("Глэб Колибров", "В работе", Color(0xFF00FF00)),
            EmployeeStub("Плесень Сергей Сырович", "В работе", Color(0xFF00FF00)),
            EmployeeStub("Некий Работник Работникович", "В работе", Color(0xFF00FF00))
        )

        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 0.dp)
            ) {
                items(employees) { employee ->
                    BaseListItem(
                        title = employee.name,
                        statusText = employee.status,
                        statusColor = employee.statusColor
                    )
                }
            }
        }
    }
}

data class EmployeeStub(
    val name: String,
    val status: String,
    val statusColor: Color
)
