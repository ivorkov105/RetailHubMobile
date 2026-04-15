package studying.diplom.retailhub.presentation.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studying.diplom.retailhub.presentation.main.profile.ProfileEvent

@Composable
fun DashboardButton(
	item: DashboardItem,
	onClick: () -> Unit
) {
	Surface(
		modifier = Modifier
			.fillMaxWidth()
			.height(80.dp)
			.clickable { onClick() },
		color = Color.White,
		shape = RoundedCornerShape(8.dp),
		shadowElevation = 4.dp
	) {
		Column(
			modifier = Modifier
				.padding(8.dp)
				.fillMaxSize(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			Text(
				text = item.label,
				fontSize = 12.sp,
				fontWeight = FontWeight.Medium,
				color = Color.DarkGray,
				lineHeight = 16.sp
			)
		}
	}
}

data class DashboardItem(
	val label: String,
	val event: ProfileEvent
)
