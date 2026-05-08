package studying.diplom.retailhub.presentation.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis.Companion.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis.Companion.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.columnSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import studying.diplom.retailhub.domain.models.analytics.AnalyticsMetric

@Composable
fun AnalyticsChart(
    metric: AnalyticsMetric,
    data: List<Pair<String, Double>>,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(data) {
        modelProducer.runTransaction {
            columnSeries {
                series(data.map { it.second })
            }
        }
    }

    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Text(
            text = metric.label,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberColumnCartesianLayer(
                    columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                        rememberLineComponent(
                            fill = Fill(Color(0xFF00A3FF)),
                            thickness = 8.dp,
                            shape = RoundedCornerShape(12.dp)
                        )
                    )
                ),
                startAxis = rememberStart(),
                bottomAxis = rememberBottom(),
            ),
            modelProducer = modelProducer,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}
