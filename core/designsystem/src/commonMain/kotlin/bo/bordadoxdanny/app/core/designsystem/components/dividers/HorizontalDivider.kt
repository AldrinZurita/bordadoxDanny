package bo.bordadoxdanny.app.core.designsystem.components.dividers

import androidx.compose.material3.HorizontalDivider as M3HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme

@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp
) {
    MaterialHorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = AppTheme.colors.textPrimary.copy(alpha = 0.1f)
    )
}

@Composable
fun MaterialHorizontalDivider(modifier: Modifier, thickness: Dp, color: Color) {
    M3HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color
    )
}
