package bo.bordadoxdanny.app.core.designsystem.components.dividers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme

// Decorative — TalkBack skips this entirely
@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp
) {
    androidx.compose.material3.HorizontalDivider(
        modifier = modifier.semantics { invisibleToUser() }, // Purely decorative
        thickness = thickness,
        color = AppTheme.colors.textPrimary.copy(alpha = 0.1f)
    )
}
