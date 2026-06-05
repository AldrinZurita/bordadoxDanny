package bo.bordadoxdanny.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.core.designsystem.util.Preview

/**
 * Contenedor estándar para secciones y listas de elementos.
 * Fondo: AppTheme.colors.surface
 * Esquinas: 12.dp redondeadas
 * Elevación: 2.dp (sombra suave)
 */
@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp))
            .background(color = AppTheme.colors.surface, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        content = content
    )
}

@Preview
@Composable
fun SectionCardPreview() {
    SectionCard {
        // Contenido de ejemplo
    }
}
