package bo.bordadoxdanny.app.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class AppColors(
    val primary: Color,
    val background: Color,
    val surface: Color,
    val textPrimary: Color,
    val isLight: Boolean,
    val error: Color,
    val success: Color,
    val warning: Color,
    val onSuccess: Color,
    val onWarning: Color,
    val onError: Color,
    val surfaceVariant: Color,
    val textSecondary: Color,
    val divider: Color
)

val LightPalette = AppColors(
    primary = Color(0xFF6200EE),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFF5F5F5),
    textPrimary = Color(0xFF000000),
    isLight = true,
    error = Color(0xFFB00020),
    success = Color(0xFF2E7D32),
    warning = Color(0xFFE65100),
    onSuccess = Color(0xFFFFFFFF),
    onWarning = Color(0xFFFFFFFF),
    onError = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFF5F5F5),
    textSecondary = Color(0xFF757575),
    divider = Color(0xFFE0E0E0)
)

val DarkPalette = AppColors(
    primary = Color(0xFFBB86FC),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    textPrimary = Color(0xFFFFFFFF),
    isLight = false,
    error = Color(0xFFCF6679),
    success = Color(0xFF66BB6A),
    warning = Color(0xFFFFA726),
    onSuccess = Color(0xFF000000),
    onWarning = Color(0xFF000000),
    onError = Color(0xFF000000),
    surfaceVariant = Color(0xFF2C2C2C),
    textSecondary = Color(0xFFAAAAAA),
    divider = Color(0xFF3A3A3A)
)

val HighContrastPalette = AppColors(
    primary = Color(0xFFFFFF00),
    background = Color(0xFF000000),
    surface = Color(0xFF000000),
    textPrimary = Color(0xFFFFFFFF),
    isLight = false,
    error = Color(0xFFFF0000),
    success = Color(0xFF00FF00),
    warning = Color(0xFFFFFF00),
    onSuccess = Color(0xFF000000),
    onWarning = Color(0xFF000000),
    onError = Color(0xFF000000),
    surfaceVariant = Color(0xFF000000),
    textSecondary = Color(0xFFFFFFFF),
    divider = Color(0xFFFFFFFF)
)
