package studying.diplom.retailhub.presentation.main.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import studying.diplom.retailhub.domain.models.analytics.ConsultantDetailStatsModel
import studying.diplom.retailhub.domain.models.analytics.AnalyticsMetric

@Composable
fun ConsultantAnalyticsSection(
    stats: ConsultantDetailStatsModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Аналитика сотрудника",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        AnalyticsChart(
            metric = AnalyticsMetric.TOTAL_REQUESTS,
            data = stats.dailyBreakdown.map { it.date to it.requestsCompleted.toDouble() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricCard(
                label = "Всего заявок",
                value = stats.consultant.totalRequests.toString(),
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                label = "Выполнено",
                value = stats.consultant.completedRequests.toString(),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricCard(
                label = "Реакция (ср)",
                value = "${stats.consultant.avgReactionSeconds} сек",
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                label = "Обслуживание (ср)",
                value = "${stats.consultant.avgServiceSeconds} сек",
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Рабочее время",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("На смене:", style = MaterialTheme.typography.bodyMedium)
                    Text("${stats.consultant.totalWorkMinutes} мин", fontWeight = FontWeight.Bold)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Занят:", style = MaterialTheme.typography.bodyMedium)
                    Text("${stats.consultant.totalBusyMinutes} мин", fontWeight = FontWeight.Bold)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Ожидание:", style = MaterialTheme.typography.bodyMedium)
                    Text("${stats.consultant.totalIdleMinutes} мин", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00A3FF)
            )
        }
    }
}
