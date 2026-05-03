package bo.bordadoxdanny.app.core.designsystem.components.icons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppIcon(
    resource: DrawableResource,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified
) {
    Image(
        painter = painterResource(resource),
        contentDescription = contentDescription,
        modifier = modifier.size(24.dp),
        colorFilter = if (tint != Color.Unspecified) ColorFilter.tint(tint) else null
    )
}
