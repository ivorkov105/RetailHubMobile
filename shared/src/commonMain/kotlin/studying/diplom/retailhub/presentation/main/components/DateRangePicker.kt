package studying.diplom.retailhub.presentation.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePicker(
    dateFrom: String?,
    dateTo: String?,
    onDateFromChange: (String?) -> Unit,
    onDateToChange: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var showFromPicker by remember { mutableStateOf(false) }
    var showToPicker by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            OutlinedTextField(
                value = dateFrom ?: "",
                onValueChange = {},
                label = { Text("Дата с") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = true
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showFromPicker = true }
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            OutlinedTextField(
                value = dateTo ?: "",
                onValueChange = {},
                label = { Text("Дата по") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = true
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showToPicker = true }
            )
        }
    }

    if (showFromPicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showFromPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = kotlinx.datetime.Instant.fromEpochMilliseconds(millis)
                            .toLocalDateTime(TimeZone.UTC).date
                        onDateFromChange(date.toString())
                    }
                    showFromPicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFromPicker = false }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showToPicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showToPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = kotlinx.datetime.Instant.fromEpochMilliseconds(millis)
                            .toLocalDateTime(TimeZone.UTC).date
                        onDateToChange(date.toString())
                    }
                    showToPicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showToPicker = false }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
