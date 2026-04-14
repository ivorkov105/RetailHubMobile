package studying.diplom.retailhub

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.koin.compose.getKoin
import studying.diplom.retailhub.domain.use_cases.auth_use_cases.GetSavedRoleUseCase
import studying.diplom.retailhub.domain.use_cases.auth_use_cases.IsAuthorizedUseCase
import studying.diplom.retailhub.presentation.auth.AuthScreen
import studying.diplom.retailhub.presentation.main.MainScreen
import studying.diplom.retailhub.presentation.theme.RetailHubTheme

@Composable
fun App() {
    val koin = getKoin()
    val isAuthorizedUseCase: IsAuthorizedUseCase = koin.get()
    val getSavedRoleUseCase: GetSavedRoleUseCase = koin.get()

    val initialScreen = remember {
        if (isAuthorizedUseCase()) {
            MainScreen(userRole = getSavedRoleUseCase())
        } else {
            AuthScreen()
        }
    }

    RetailHubTheme {
        Navigator(screen = initialScreen) { navigator ->
            SlideTransition(navigator)
        }
    }
}
