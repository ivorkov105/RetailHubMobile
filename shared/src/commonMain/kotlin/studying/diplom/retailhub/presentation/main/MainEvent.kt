package studying.diplom.retailhub.presentation.main

import cafe.adriel.voyager.navigator.tab.Tab

sealed interface MainEvent {
    data class SelectTab(val tab: Tab) : MainEvent
    data object OnCloseApp : MainEvent
    data object OnLogout : MainEvent
}
