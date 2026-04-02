package studying.diplom.retailhub.presentation.main.requests.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import studying.diplom.retailhub.domain.models.request.RequestModel
import studying.diplom.retailhub.domain.models.request.RequestStatus

@Composable
fun RequestsListItem(
    request: RequestModel,
    currentUserFullName: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val assignedFullName = listOfNotNull(request.assignedUserFirstName, request.assignedUserLastName)
        .filter { it.isNotBlank() }
        .joinToString(" ")
        .trim()

    val isAssigned = assignedFullName.isNotEmpty()
    val isAssignedToMe = isAssigned && assignedFullName.equals(currentUserFullName.trim(), ignoreCase = true)

    val isEnabled = when (request.status) {
        RequestStatus.CREATED, RequestStatus.WAITING -> true
        RequestStatus.ASSIGNED -> isAssignedToMe
        RequestStatus.ESCALATED -> !isAssigned || isAssignedToMe
        else -> false
    }

    val buttonColor = when (request.status) {
        RequestStatus.COMPLETED -> Color(0xFF4CAF50) // Green
        RequestStatus.CANCELED -> Color.Gray
        RequestStatus.ASSIGNED -> if (isAssignedToMe) Color(0xFF3DA9FC) else Color.Gray
        RequestStatus.ESCALATED -> if (isAssigned && !isAssignedToMe) Color.Gray else Color(0xFFF44336) // Red
        else -> MaterialTheme.colorScheme.primary
    }

    val buttonText = when (request.status) {
        RequestStatus.COMPLETED -> "Завершено"
        RequestStatus.CANCELED -> "Отменено"
        RequestStatus.WAITING -> "В ожидании"
        RequestStatus.ASSIGNED -> if (isAssignedToMe) "Завершить" else "Принято"
        RequestStatus.ESCALATED -> when {
            isAssignedToMe -> "Завершить"
            isAssigned -> "Занято"
            else -> "СРОЧНО"
        }
        else -> "Принять"
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Отдел: ${request.departmentName}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                StatusBadge(status = request.status)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onButtonClick,
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    disabledContainerColor = buttonColor
                ),
                enabled = isEnabled
            ) {
                Text(
                    text = buttonText,
                    color = if (isEnabled) Color.White else Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun StatusBadge(status: RequestStatus) {
    val color = when (status) {
        RequestStatus.CREATED -> Color(0xFF2196F3)
        RequestStatus.WAITING -> Color(0xFFFF9800)
        RequestStatus.ESCALATED -> Color(0xFFF44336)
        RequestStatus.ASSIGNED -> Color(0xFF9C27B0)
        RequestStatus.COMPLETED -> Color(0xFF4CAF50)
        RequestStatus.CANCELED -> Color(0xFF757575)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = status.name,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}
