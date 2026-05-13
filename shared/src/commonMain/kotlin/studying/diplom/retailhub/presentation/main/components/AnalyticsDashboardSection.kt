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
import studying.diplom.retailhub.domain.models.analytics.Period

@Composable
fun AnalyticsDashboardSection(
	dashboard: AnalyticsDashboardModel,
	selectedPeriod: Period,
	onPeriodSelected: (Period) -> Unit,
	modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Аналитика",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

	    PeriodSelector(
	        selectedPeriod = selectedPeriod,
	        onPeriodSelected = onPeriodSelected
		)

	    Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricCard(
                modifier = Modifier.weight(1f),
                metric = AnalyticsMetric.TOTAL_REQUESTS,
                value = dashboard.totalRequestsToday.toString()
            )
            MetricCard(
                modifier = Modifier.weight(1f),
                metric = AnalyticsMetric.COMPLETED_REQUESTS,
                value = dashboard.completedRequestsToday.toString()
            )
        }

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
