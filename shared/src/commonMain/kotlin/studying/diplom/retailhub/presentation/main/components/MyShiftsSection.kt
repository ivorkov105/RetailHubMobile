package studying.diplom.retailhub.presentation.main.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import studying.diplom.retailhub.data.entities.shift.ShiftEntity

@Composable
fun MyShiftsSection(
    shifts: List<ShiftEntity>,
    isShiftsLoading: Boolean,
    dateFrom: String?,
    dateTo: String?,
    onDateFromChange: (String?) -> Unit,
    onDateToChange: (String?) -> Unit,
    currentStatus: String,
    onStartShiftClick: () -> Unit,
    onEndShiftClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isBusy = currentStatus == "BUSY"
    val isOffline = currentStatus == "OFFLINE"
    val accentColor = Color(0xFF00A3FF)

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Мои смены",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        DateRangePicker(
            dateFrom = dateFrom,
            dateTo = dateTo,
            onDateFromChange = onDateFromChange,
            onDateToChange = onDateToChange
        )
        Spacer(modifier = Modifier.height(16.dp))

        ShiftsList(
            shifts = shifts,
            isLoading = isShiftsLoading,
            dateFrom = dateFrom,
            dateTo = dateTo
        )

        Spacer(modifier = Modifier.height(24.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .then(
                    if (isBusy) Modifier
                    else Modifier.clickable {
                        if (isOffline) onStartShiftClick()
                        else onEndShiftClick()
                    }
                ),
            color = if (isOffline) accentColor else Color.Transparent,
            shape = RoundedCornerShape(12.dp),
            border = if (!isOffline) BorderStroke(
                1.dp,
                if (isBusy) Color.Gray else accentColor
            ) else null
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = if (isOffline) "Начать смену" else "Завершить смену",
                    color = when {
                        isOffline -> Color.White
                        isBusy    -> Color.Gray
                        else      -> accentColor
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
