package studying.diplom.retailhub.presentation.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import studying.diplom.retailhub.domain.models.qr_code.QrCodeModel

@Composable
fun QrCodeItem(
	qrCode: QrCodeModel,
	isExpanded: Boolean,
	onToggle: () -> Unit,
	onDeactivate: () -> Unit,
	onDownload: () -> Unit
) {
	var showDeactivateDialog by remember { mutableStateOf(false) }

	if (showDeactivateDialog) {
		DeleteConfirmDialog(
			title = "Деактивировать QR-код?",
			text = "Вы уверены, что хотите деактивировать этот QR-код? Он перестанет работать для клиентов.",
			onDismiss = { showDeactivateDialog = false },
			onConfirm = {
				showDeactivateDialog = false
				onDeactivate()
			}
		)
	}

	Card(
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(12.dp))
			.clickable { onToggle() },
		elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
		colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
	) {
		Column(modifier = Modifier.padding(16.dp)) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Column(modifier = Modifier.weight(1f)) {
					Text(
						text = qrCode.label,
						style = MaterialTheme.typography.titleMedium,
						fontWeight = FontWeight.Bold,
						maxLines = 2,
						overflow = TextOverflow.Ellipsis
					)
					Text(
						text = qrCode.departmentName,
						style = MaterialTheme.typography.bodySmall,
						color = Color.Gray
					)
				}

				Spacer(modifier = Modifier.width(8.dp))

				Surface(
					color = if (qrCode.isActive) Color(0xFF4CAF50).copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.1f),
					shape = RoundedCornerShape(8.dp)
				) {
					Text(
						text = if (qrCode.isActive) "Активен" else "Неактивен",
						modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
						style = MaterialTheme.typography.labelSmall,
						color = if (qrCode.isActive) Color(0xFF4CAF50) else Color.Gray,
						maxLines = 1
					)
				}
			}

			AnimatedVisibility(visible = isExpanded) {
				Column(modifier = Modifier.padding(top = 16.dp)) {
					HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))

					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.spacedBy(8.dp)
					) {
						Button(
							onClick = onDownload,
							modifier = Modifier.weight(1f),
							contentPadding = PaddingValues(horizontal = 4.dp),
							colors = ButtonDefaults.buttonColors(
								containerColor = Color(0xFF00A3FF),
								contentColor = Color.White
							),
							shape = RoundedCornerShape(8.dp)
						) {
							Text(
								"Скачать",
								maxLines = 1,
								style = MaterialTheme.typography.labelLarge
							)
						}

						Button(
							onClick = {
								showDeactivateDialog = true
							},
							modifier = Modifier.weight(1f),
							contentPadding = PaddingValues(horizontal = 4.dp),
							colors = ButtonDefaults.buttonColors(
								containerColor = Color.Red,
								contentColor = Color.White
							),
							shape = RoundedCornerShape(8.dp),
							enabled = qrCode.isActive
						) {
							Text(
								"Деактивировать",
								maxLines = 1,
								overflow = TextOverflow.Clip,
								style = MaterialTheme.typography.labelMedium, softWrap = false
							)
						}
					}
				}
			}
		}
	}
}
