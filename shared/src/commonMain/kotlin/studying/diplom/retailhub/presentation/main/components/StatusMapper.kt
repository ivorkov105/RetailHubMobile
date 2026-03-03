package studying.diplom.retailhub.presentation.main.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import studying.diplom.retailhub.data.enteties.request.RequestStatus
import studying.diplom.retailhub.presentation.theme.*

data class StatusUiModel(
    val text: String,
    val color: Color
)

@Composable
fun RequestStatus.toUiModel(): StatusUiModel {
    return when (this) {
        RequestStatus.IN_PROGRESS -> StatusUiModel("В работе", LightStatusActive)
        RequestStatus.CREATED -> StatusUiModel("В ожидании", LightStatusInactive)
        RequestStatus.CANCELED -> StatusUiModel("Задерживается", LightStatusCritical) // Пример сопоставления
        RequestStatus.COMPLETED -> StatusUiModel("Завершено", LightStatusActive)
    }
}
