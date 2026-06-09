package bo.bordadoxdanny.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.core.designsystem.util.Preview

/**
 * Tarjeta estándar para visualización de Órdenes de Trabajo.
 */
@Composable
fun OrderCard(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    statusBadge: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AppTheme.colors.surface)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = AppTheme.typography.titleMedium,
                    color = AppTheme.colors.textPrimary,
                    modifier = Modifier.weight(1f)
                )
                statusBadge()
            }

            content()
        }
    }
}

@Preview
@Composable
fun OrderCardPreview() {
    OrderCard(
        title = "Juan Pérez",
        onClick = {},
        statusBadge = {
            // Ejemplo de uso con el StatusBadge existente
            //StatusBadge(text = "Pendiente", color = AppTheme.colors.warning)
        }
    ) {
        Text(
            text = "Detalle de la orden: 2 Poleras bordadas",
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.textSecondary
        )
    }
}