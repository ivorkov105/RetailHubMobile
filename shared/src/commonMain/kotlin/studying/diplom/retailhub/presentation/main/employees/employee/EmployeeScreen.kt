package studying.diplom.retailhub.presentation.main.employees.employee

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf
import studying.diplom.retailhub.domain.models.user.UserModel
import studying.diplom.retailhub.presentation.auth.AuthScreen
import studying.diplom.retailhub.presentation.main.components.InfoBlock
import studying.diplom.retailhub.presentation.main.utils.ScreenMode
import studying.diplom.retailhub.presentation.main.utils.UserRoles
import studying.diplom.retailhub.resources.Res
import studying.diplom.retailhub.resources.ic_arrow_left
import studying.diplom.retailhub.resources.ic_edit

class EmployeeScreen(
    private val initialEmployee: UserModel? = null,
    private val mode: ScreenMode = ScreenMode.SHOW
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel: EmployeeViewModel = koinScreenModel { parametersOf(initialEmployee) }
        val state by screenModel.state.collectAsState()
        var currentMode by remember { mutableStateOf(mode) }
        var expanded by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            if (currentMode != ScreenMode.CREATE) screenModel.onEvent(EmployeeEvent.OnLoadEmployee)

            screenModel.navigationEvents.collectLatest { event ->
                when (event) {
                    EmployeeNavigationEvent.NavigateBack -> navigator.pop()
                    EmployeeNavigationEvent.NavigateToAuth -> navigator.parent?.replace(AuthScreen())
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            when (currentMode) {
                                ScreenMode.CREATE -> "Создание сотрудника"
                                ScreenMode.UPDATE -> "Редактирование сотрудника"
                                else -> "Профиль сотрудника"
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { screenModel.onEvent(EmployeeEvent.OnBackClick) }) {
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
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    state.error?.let { error ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    InfoBlock(
                        label = "Имя",
                        value = state.data?.firstName ?: "",
                        mode = currentMode,
                        onValueChange = { screenModel.onEvent(EmployeeEvent.OnNameChange(it)) }
                    )

                    InfoBlock(
                        label = "Фамилия",
                        value = state.data?.lastName ?: "",
                        mode = currentMode,
                        onValueChange = { screenModel.onEvent(EmployeeEvent.OnSurnameChange(it)) }
                    )

                    InfoBlock(
                        label = "Номер телефона",
                        value = state.data?.phoneNumber ?: "",
                        mode = currentMode,
                        onValueChange = { screenModel.onEvent(EmployeeEvent.OnPhoneNumberChange(it)) }
                    )

                    if (currentMode == ScreenMode.CREATE) {
                        InfoBlock(
                            label = "Пароль",
                            value = state.data?.password ?: "",
                            mode = currentMode,
                            onValueChange = { screenModel.onEvent(EmployeeEvent.OnPasswordChange(it)) }
                        )
                    }

                    // Выбор роли
                    if (currentMode == ScreenMode.SHOW || currentMode == ScreenMode.UPDATE) {
                        InfoBlock(label = "Роль", value = state.data?.role ?: "", mode = ScreenMode.SHOW)
                    } else {
                        Column {
                            Text(
                                text = "Роль",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = state.data?.role ?: "",
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                    modifier = Modifier.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true).fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    UserRoles.entries.forEach { role ->
                                        DropdownMenuItem(
                                            text = { Text(role.name) },
                                            onClick = {
                                                screenModel.onEvent(EmployeeEvent.OnRoleChange(role))
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Список отделов (показываем для всех ролей)
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Отделы",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        
                        if (currentMode == ScreenMode.SHOW) {
                            val userDepartments = state.data?.departments ?: emptyList()

                            if (userDepartments.isEmpty()) {
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "Отделы не выбраны",
                                        modifier = Modifier.padding(12.dp),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    userDepartments.forEach { dept ->
                                        Surface(
                                            modifier = Modifier.fillMaxWidth(),
                                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = dept.name,
                                                modifier = Modifier.padding(12.dp),
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            // Режим создания или редактирования: список всех отделов с чекбоксами
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                state.availableDepartments.forEach { dept ->
                                    val isChecked = state.data?.departments?.any { it.id == dept.id } == true
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(min = 48.dp)
                                            .toggleable(
                                                value = isChecked,
                                                role = Role.Checkbox,
                                                onValueChange = { screenModel.onEvent(EmployeeEvent.OnDepartmentToggle(dept.id)) }
                                            )
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = isChecked,
                                            onCheckedChange = null // Обрабатывается в Row.toggleable
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = dept.name,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                                
                                if (state.availableDepartments.isEmpty()) {
                                    Text(
                                        text = "Список доступных отделов пуст",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }

                    if (currentMode == ScreenMode.CREATE || currentMode == ScreenMode.UPDATE) {
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(
                            onClick = {
                                if (currentMode == ScreenMode.CREATE) screenModel.onEvent(EmployeeEvent.OnSaveClick)
                                else screenModel.onEvent(EmployeeEvent.OnUpdateClick)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Сохранить")
                        }
                    }

                    if (currentMode == ScreenMode.UPDATE) {
                         OutlinedButton(
                            onClick = { screenModel.onEvent(EmployeeEvent.OnDeleteClick) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Удалить сотрудника")
                        }
                    }
                }
            }
        }
    }
}
