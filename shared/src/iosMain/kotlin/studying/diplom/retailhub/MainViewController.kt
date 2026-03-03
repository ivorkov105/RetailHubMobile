package studying.diplom.retailhub

import androidx.compose.ui.window.ComposeUIViewController
import studying.diplom.retailhub.di.initKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}
