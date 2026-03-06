package studying.diplom.retailhub

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import studying.diplom.retailhub.presentation.auth.AuthScreen
import studying.diplom.retailhub.presentation.theme.RetailHubTheme

@Composable
fun App() {
    RetailHubTheme {
        Navigator(screen = AuthScreen()) { navigator ->
            SlideTransition(navigator)
        }
    }
}
