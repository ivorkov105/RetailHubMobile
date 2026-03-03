package studying.diplom.retailhub.presentation.main.departments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.painterResource
import studying.diplom.retailhub.presentation.main.components.BaseListItem
import studying.diplom.retailhub.resources.Res
import studying.diplom.retailhub.resources.ic_departments

object DepartmentTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Отделы"
            val icon = painterResource(Res.drawable.ic_departments)

            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val departments = listOf(
            DepartmentStub("Мясной отдел", "Некое описание (?)"),
            DepartmentStub("Молочный отдел", "Некое описание (?)"),
            DepartmentStub("Овощи и фрукты", "Некое описание (?)"),
            DepartmentStub("Заморозка", "Некое описание (?)"),
            DepartmentStub("Выпечка", "Некое описание (?)"),
            DepartmentStub("Напитки", "Некое описание (?)")
        )

        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 0.dp)
            ) {
                items(departments) { department ->
                    BaseListItem(
                        title = department.name,
                        subtitle = department.description
                    )
                }
            }
        }
    }
}

data class DepartmentStub(
    val name: String,
    val description: String
)
