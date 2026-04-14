package studying.diplom.retailhub.presentation.main.departments.department

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import studying.diplom.retailhub.domain.models.shop.DepartmentModel
import studying.diplom.retailhub.presentation.auth.AuthScreen
import studying.diplom.retailhub.presentation.main.components.InfoBlock
import studying.diplom.retailhub.presentation.main.utils.ScreenMode
import studying.diplom.retailhub.resources.Res
import studying.diplom.retailhub.resources.ic_arrow_left
import studying.diplom.retailhub.resources.ic_edit

class DepartmentScreen(
    private val initialDepartment: DepartmentModel? = null,
    private val mode: ScreenMode = ScreenMode.SHOW
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel: DepartmentViewModel = koinScreenModel { parametersOf(initialDepartment) }
        val state by screenModel.state.collectAsState()
        var currentMode by remember { mutableStateOf(mode) }

        LaunchedEffect(Unit) {
			if (currentMode != ScreenMode.CREATE) screenModel.onEvent(DepartmentEvent.OnLoadDepartment)

            screenModel.navigationEvents.collectLatest { event ->
                when (event) {
                    DepartmentNavigationEvent.NavigateBack -> navigator.pop()
                    DepartmentNavigationEvent.NavigateToAuth -> navigator.parent?.replace(AuthScreen())
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
	                        when (currentMode) {
		                        ScreenMode.CREATE -> "Создание отдела"
		                        ScreenMode.UPDATE -> "Редактирование отдела"
		                        else              -> "Просмотр отдела"
	                        }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { screenModel.onEvent(DepartmentEvent.OnBackClick) }) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_arrow_left),
                                contentDescription = "Назад",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    actions = {
                        if (currentMode == ScreenMode.SHOW) {
                            IconButton(onClick = { currentMode = ScreenMode.UPDATE }) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_edit),
                                    contentDescription = "Редактировать",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
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
                    InfoBlock(
                        label = "Название отдела",
                        value = state.data?.name ?: "",
                        mode = currentMode,
                        onValueChange = { screenModel.onEvent(DepartmentEvent.OnNameChange(it)) }
                    )

                    InfoBlock(
                        label = "Описание",
                        value = state.data?.description ?: "",
                        mode = currentMode,
                        onValueChange = { screenModel.onEvent(DepartmentEvent.OnDescriptionChange(it)) }
                    )

                    if (currentMode == ScreenMode.CREATE || currentMode == ScreenMode.UPDATE) {
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = {
                                if (currentMode == ScreenMode.CREATE) screenModel.onEvent(DepartmentEvent.OnSaveClick)
                                else screenModel.onEvent(DepartmentEvent.OnUpdateClick)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Сохранить")
                        }
                    }
                }
            }
        }
    }
}
