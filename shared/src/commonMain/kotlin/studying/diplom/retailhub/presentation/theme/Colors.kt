package studying.diplom.retailhub.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Голубой акцент
internal val LightPrimary = Color(0xFF3DA9FC)
internal val LightOnPrimary = Color(0xFFFFFFFF)

// Фоны и карточки
internal val LightBackground = Color(0xFFF0F4F8)
internal val LightSurface = Color(0xFFFFFFFF)
internal val LightSurfaceVariant = Color(0xFFE2E8F0)

// Текст
internal val LightOnBackground = Color(0xFF1E1E1E)
internal val LightOnSurface = Color(0xFF1E1E1E)

// Оранжевый акцент
internal val DarkPrimary = Color(0xFFFF8A00)
internal val DarkOnPrimary = Color(0xFF121212)

// Фоны и карточки
internal val DarkBackground = Color(0xFF121212)
internal val DarkSurface = Color(0xFF242424)
internal val DarkSurfaceVariant = Color(0xFF383838)

// Текст
internal val DarkOnBackground = Color(0xFFE0E0E0)
internal val DarkOnSurface = Color(0xFFE0E0E0)

//Дополнительные цвета
internal val LightStatusActive = Color(0xFF00FF15)
internal val LightStatusCritical = Color(0xFFFF0000)
internal val LightStatusInactive = Color(0xFF777777)

internal val DarkStatusActive = Color(0xFF4CAF50)
internal val DarkStatusCritical = Color(0xFFF44336)
internal val DarkStatusInactive = Color(0xFF868686)

internal val LightColorScheme = lightColorScheme(
	primary = LightPrimary,
	onPrimary = LightOnPrimary,
	background = LightBackground,
	onBackground = LightOnBackground,
	surface = LightSurface,
	onSurface = LightOnSurface,
	surfaceVariant = LightSurfaceVariant
)

internal val DarkColorScheme = darkColorScheme(
	primary = DarkPrimary,
	onPrimary = DarkOnPrimary,
	background = DarkBackground,
	onBackground = DarkOnBackground,
	surface = DarkSurface,
	onSurface = DarkOnSurface,
	surfaceVariant = DarkSurfaceVariant
)