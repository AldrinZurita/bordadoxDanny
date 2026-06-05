package bo.bordadoxdanny.app.core.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.core.designsystem.util.Preview

/**
 * Muestra un monto monetario destacado con prefijo "Bs.".
 * Usa AppTheme.typography.displaySmall y el color recibido.
 */
@Composable
fun AmountDisplay(
    amount: String,
    color: Color = AppTheme.colors.textPrimary,
    label: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        label?.let {
            Text(
                text = it,
                style = AppTheme.typography.labelSmall,
                color = AppTheme.colors.textSecondary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Bs. ",
                style = AppTheme.typography.titleMedium,
                color = color
            )
            Text(
                text = amount,
                style = AppTheme.typography.displaySmall,
                color = color
            )
        }
    }
}

@Preview
@Composable
fun AmountDisplayPreview() {
    AmountDisplay(
        amount = "1,250.00",
        label = "Total a pagar",
        color = AppTheme.colors.success
    )
}
