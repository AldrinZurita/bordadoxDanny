package bo.bordadoxdanny.app.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

enum class ThemeMode { LIGHT, DARK, HIGH_CONTRAST }

val LocalColors = staticCompositionLocalOf { LightPalette }
internal val LocalTypography = staticCompositionLocalOf { DefaultTypography }

object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}

@Composable
fun DsTheme(
    mode: ThemeMode = if (isSystemInDarkTheme()) ThemeMode.DARK else ThemeMode.LIGHT,
    content: @Composable () -> Unit
) {
    val colors = when (mode) {
        ThemeMode.LIGHT -> LightPalette
        ThemeMode.DARK -> DarkPalette
        ThemeMode.HIGH_CONTRAST -> HighContrastPalette
    }

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides DefaultTypography
    ) {
        content()
    }
}
