package studying.diplom.retailhub.presentation.main.departments.department_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import studying.diplom.retailhub.presentation.auth.AuthScreen
import studying.diplom.retailhub.presentation.main.components.BaseListItem
import studying.diplom.retailhub.presentation.main.departments.department.DepartmentScreen
import studying.diplom.retailhub.presentation.main.utils.ScreenMode
import studying.diplom.retailhub.resources.Res
import studying.diplom.retailhub.resources.ic_departments

object DepartmentsListTab : Tab {
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
        val navigator = LocalNavigator.currentOrThrow
        val screenModel: DepartmentsListViewModel = koinScreenModel()
        val state by screenModel.state.collectAsState()

        LaunchedEffect(Unit) {
            screenModel.onEvent(DepartmentsListEvent.LoadList)
            
            screenModel.navigationEvents.collectLatest { event ->
                when (event) {
                    is DepartmentsListNavigationEvent.NavigateToDepartment -> {
                        navigator.parent?.push(DepartmentScreen(event.department, ScreenMode.SHOW))
                    }
                    DepartmentsListNavigationEvent.NavigateToAuth -> {
                        navigator.parent?.replace(AuthScreen())
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
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(state.departments) { department ->
                        BaseListItem(
                            title = department.name,
                            subtitle = department.description,
                            onClick = { screenModel.onEvent(DepartmentsListEvent.OnDepartmentClick(department)) }
                        )
                    }
                }
            }
        }
    }
}
