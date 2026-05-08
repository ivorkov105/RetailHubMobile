package studying.diplom.retailhub.presentation.main.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import studying.diplom.retailhub.domain.models.analytics.AnalyticsDashboardModel
import studying.diplom.retailhub.domain.models.analytics.AnalyticsMetric

@Composable
fun AnalyticsDashboardSection(
    dashboard: AnalyticsDashboardModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Аналитика за сегодня",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // График сравнения заявок
        AnalyticsChart(
            metric = AnalyticsMetric.TOTAL_REQUESTS,
            data = listOf(
                "Всего" to dashboard.totalRequestsToday.toDouble(),
                "Выполнено" to dashboard.completedRequestsToday.toDouble()
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricCard(
                modifier = Modifier.weight(1f),
                metric = AnalyticsMetric.AVG_REACTION_TIME,
                value = "${dashboard.avgReactionTimeSeconds} сек"
            )
            MetricCard(
                modifier = Modifier.weight(1f),
                metric = AnalyticsMetric.AVG_SERVICE_TIME,
                value = "${dashboard.avgServiceTimeSeconds} сек"
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        MetricCard(
            modifier = Modifier.fillMaxWidth(),
            metric = AnalyticsMetric.ACTIVE_CONSULTANTS,
            value = dashboard.activeConsultants.toString()
        )
    }
}

@Composable
private fun MetricCard(
    metric: AnalyticsMetric,
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
                text = metric.label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00A3FF)
            )
        }
    }
}
