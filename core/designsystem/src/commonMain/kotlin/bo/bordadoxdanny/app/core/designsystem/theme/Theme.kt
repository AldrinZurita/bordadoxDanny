package bo.bordadoxdanny.app.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

enum class ThemeMode { LIGHT, DARK, HIGH_CONTRAST }

val LocalColors = staticCompositionLocalOf { LightPalette }
internal val LocalTypography = staticCompositionLocalOf { DefaultTypography }

object AppTheme {
    val colors: AppColors @Composable get() = LocalColors.current
    val typography: Typography @Composable get() = LocalTypography.current
}

@Composable
fun DsTheme(
    mode: ThemeMode = if (isSystemInDarkTheme()) ThemeMode.DARK else ThemeMode.LIGHT,
    content: @Composable () -> Unit
) {
    val colors = when (mode) {
        ThemeMode.LIGHT          -> LightPalette
        ThemeMode.DARK           -> DarkPalette
        ThemeMode.HIGH_CONTRAST  -> HighContrastPalette
    }

    // Mapeamos tus colores personalizados al ColorScheme de Material 3
    val colorScheme = if (colors.isLight) {
        lightColorScheme(
            primary = colors.primary,
            background = colors.background,
            surface = colors.surface,
            onBackground = colors.textPrimary,
            onSurface = colors.textPrimary
        )
    } else {
        darkColorScheme(
            primary = colors.primary,
            background = colors.background,
            surface = colors.surface,
            onBackground = colors.textPrimary,
            onSurface = colors.textPrimary
        )
    }

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides DefaultTypography
    ) {
        // 🔥 ESTA ES LA PIEZA CLAVE: MaterialTheme debe envolver el contenido
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}
