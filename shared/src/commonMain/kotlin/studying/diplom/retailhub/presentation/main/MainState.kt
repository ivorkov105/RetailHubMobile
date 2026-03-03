package studying.diplom.retailhub.presentation.main

import cafe.adriel.voyager.navigator.tab.Tab
import studying.diplom.retailhub.presentation.main.employees.EmployeesTab

data class MainState(
    val currentTab: Tab = EmployeesTab
)
