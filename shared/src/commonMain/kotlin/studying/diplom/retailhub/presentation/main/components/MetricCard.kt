package studying.diplom.retailhub.presentation.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import studying.diplom.retailhub.domain.models.analytics.AnalyticsMetric

@Composable
fun MetricCard(
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