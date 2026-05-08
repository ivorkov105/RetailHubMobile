package studying.diplom.retailhub.presentation.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import studying.diplom.retailhub.domain.models.request.RequestStatus
import studying.diplom.retailhub.resources.Res
import studying.diplom.retailhub.resources.ic_arrow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestsFilterDialog(
    selectedStatus: RequestStatus?,
    onStatusChange: (RequestStatus?) -> Unit,
    dateFrom: String?,
    onDateFromChange: (String?) -> Unit,
    dateTo: String?,
    onDateToChange: (String?) -> Unit,
    onDismiss: () -> Unit,
    onApply: () -> Unit,
    onClear: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Фильтры") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedTextField(
                        value = selectedStatus?.name ?: "Все статусы",
                        onValueChange = {},
                        label = { Text("Статус") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth().clickable { expanded = true },
                        trailingIcon = {
                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_arrow),
                                    contentDescription = null,
                                    modifier = Modifier.rotate(if (expanded) 180f else 0f)
                                )
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.7f)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Все статусы") },
                            onClick = {
                                onStatusChange(null)
                                expanded = false
                            }
                        )
                        RequestStatus.entries.forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status.name) },
                                onClick = {
                                    onStatusChange(status)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                DateRangePicker(
                    dateFrom = dateFrom,
                    dateTo = dateTo,
                    onDateFromChange = onDateFromChange,
                    onDateToChange = onDateToChange
                )
            }
        },
        confirmButton = {
            Button(onClick = onApply) {
                Text("Применить")
            }
        },
        dismissButton = {
            TextButton(onClick = onClear) {
                Text("Сбросить")
            }
        }
    )
}
