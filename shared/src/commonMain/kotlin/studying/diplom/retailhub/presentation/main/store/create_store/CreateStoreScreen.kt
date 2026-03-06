package studying.diplom.retailhub.presentation.main.store.create_store

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf
import studying.diplom.retailhub.domain.models.shop.StoreModel
import studying.diplom.retailhub.resources.Res
import studying.diplom.retailhub.resources.ic_arrow_left

class CreateStoreScreen(private val initialStore: StoreModel? = null) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel: CreateStoreViewModel = koinScreenModel { parametersOf(initialStore) }
        val state by screenModel.state.collectAsState()

        LaunchedEffect(Unit) {
            screenModel.navigationEvents.collectLatest { event ->
                when (event) {
                    CreateStoreNavigationEvent.NavigateBack -> {
                        navigator.pop()
                    }
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (initialStore == null) "Создание магазина" else "Редактирование магазина") },
                    navigationIcon = {
                        IconButton(onClick = { screenModel.onEvent(CreateStoreEvent.OnBackClick) }) {
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { screenModel.onEvent(CreateStoreEvent.OnNameChange(it)) },
                    label = { Text("Название магазина") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = state.address,
                    onValueChange = { screenModel.onEvent(CreateStoreEvent.OnAddressChange(it)) },
                    label = { Text("Адрес") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = state.description,
                    onValueChange = { screenModel.onEvent(CreateStoreEvent.OnDescriptionChange(it)) },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = RoundedCornerShape(8.dp)
                )

                if (state.error != null) {
                    Text(
                        text = state.error!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                
                Button(
                    onClick = { screenModel.onEvent(CreateStoreEvent.OnSaveClick) },
                    enabled = !state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Сохранить")
                    }
                }
            }
        }
    }
}
