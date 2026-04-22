package studying.diplom.retailhub.presentation.main.qr.qr_list

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.painterResource
import studying.diplom.retailhub.domain.models.qr_code.QrCodeModel
import studying.diplom.retailhub.presentation.components.StatusPopup
import studying.diplom.retailhub.resources.Res
import studying.diplom.retailhub.resources.ic_arrow_left

class QrListScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel: QrListViewModel = koinScreenModel()
        val state by screenModel.state.collectAsState()

        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier.blur(if (state.downloadStatus != null) 10.dp else 0.dp),
                topBar = {
                    TopAppBar(
                        title = { Text("Список QR-кодов") },
                        navigationIcon = {
                            IconButton(onClick = { navigator.pop() }) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_arrow_left),
                                    contentDescription = "Назад",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    )
                }
            ) { paddingValues ->
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                    if (state.isLoading && state.qrCodes.isEmpty()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else if (state.qrCodes.isEmpty()) {
                        Text(
                            text = "QR-коды не найдены",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Gray
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.qrCodes) { qrCode ->
                                QrCodeItem(
                                    qrCode = qrCode,
                                    isExpanded = state.expandedQrCodeId == qrCode.id,
                                    onToggle = { screenModel.toggleExpand(qrCode.id) },
                                    onDeactivate = { screenModel.deactivateQrCode(qrCode.id) },
                                    onDownload = { screenModel.downloadQrCode(qrCode.id) }
                                )
                            }
                        }
                    }

                    if (state.error != null && state.downloadStatus == null) {
                        Snackbar(
                            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ) {
                            Text(state.error!!)
                        }
                    }
                }
            }

            // Используем универсальную всплывашку
            val (text, color) = when (val status = state.downloadStatus) {
                is DownloadStatus.Success -> "Скачано" to Color(0xFF4CAF50)
                is DownloadStatus.Error -> "Ошибка" to Color.Red
                else -> "" to Color.Transparent
            }

            StatusPopup(
                visible = state.downloadStatus != null,
                text = text,
                color = color,
                onDismiss = { screenModel.clearStatus() }
            )
        }
    }
}

@Composable
fun QrCodeItem(
    qrCode: QrCodeModel,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onDeactivate: () -> Unit,
    onDownload: () -> Unit
) {
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
                            onClick = onDeactivate,
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
