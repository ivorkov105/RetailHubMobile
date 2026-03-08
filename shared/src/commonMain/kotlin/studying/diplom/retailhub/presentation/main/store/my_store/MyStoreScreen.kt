package studying.diplom.retailhub.presentation.main.store.my_store

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import studying.diplom.retailhub.presentation.main.components.InfoBlock
import studying.diplom.retailhub.resources.Res
import studying.diplom.retailhub.resources.ic_arrow_left

class MyStoreScreen : Screen {

	@OptIn(ExperimentalMaterial3Api::class)
	@Composable
	override fun Content() {
		val navigator = LocalNavigator.currentOrThrow
		val screenModel: MyStoreViewModel = koinScreenModel()
		val state by screenModel.state.collectAsState()

		LaunchedEffect(Unit) {
			screenModel.onEvent(MyStoreEvent.LoadStore)

			screenModel.navigationEvents.collectLatest { event ->
				when (event) {
					MyStoreNavigationEvent.NavigateBack -> navigator.pop()
				}
			}
		}

		Scaffold(
			topBar = {
				TopAppBar(
					title = { Text("Мой магазин") },
					navigationIcon = {
						IconButton(onClick = { screenModel.onEvent(MyStoreEvent.OnBackClick) }) {
							Icon(
								painter = painterResource(Res.drawable.ic_arrow_left),
								contentDescription = "Назад",
								modifier = Modifier
									.size(24.dp)
							)
						}
					}
				)
			}
		) { paddingValues ->
			if (state.isLoading) {
				Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
					CircularProgressIndicator()
				}
			} else {
				Column(
					modifier = Modifier
						.fillMaxSize()
						.padding(paddingValues)
						.padding(16.dp),
					verticalArrangement = Arrangement.spacedBy(24.dp)
				) {
					state.store?.let { store ->
						InfoBlock(label = "Название магазина", value = store.name)
						InfoBlock(label = "Адрес", value = store.address)
					} ?: run {
						if (state.error != null) {
							Text(text = state.error!!, color = Color.Red)
						} else {
							Text(text = "Данные о магазине отсутствуют")
						}
					}
				}
			}
		}
	}
}
