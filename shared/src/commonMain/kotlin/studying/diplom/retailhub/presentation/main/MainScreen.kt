package studying.diplom.retailhub.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import studying.diplom.retailhub.presentation.main.departments.department_list.DepartmentsListTab
import studying.diplom.retailhub.presentation.main.employees.employees_list.EmployeesTab
import studying.diplom.retailhub.presentation.main.profile.ProfileTab

class MainScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel: MainViewModel = getScreenModel()
        val state by screenModel.state.collectAsState()

        TabNavigator(EmployeesTab) { tabNavigator ->
            Scaffold(
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
                            TabNavigationItem(EmployeesTab, state, screenModel)
                            TabNavigationItem(DepartmentsListTab, state, screenModel)
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
