package studying.diplom.retailhub.presentation.main

import cafe.adriel.voyager.navigator.tab.Tab

sealed class MainEvent {
    data class SelectTab(val tab: Tab) : MainEvent()
}
