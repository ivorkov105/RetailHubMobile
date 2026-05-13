package studying.diplom.retailhub.presentation.main.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import studying.diplom.retailhub.data.entities.shift.ShiftEntity

@Composable
fun ShiftsList(
    shifts: List<ShiftEntity>,
    isLoading: Boolean,
    dateFrom: String?,
    dateTo: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 300.dp)
    ) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        } else if (shifts.isEmpty()) {
            Text(
                text = if (dateFrom == null || dateTo == null) "Выберите период" else "Смен не найдено",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                shifts.forEach { shift ->
                    ShiftItem(shift = shift)
                }
            }
        }
    }
}
