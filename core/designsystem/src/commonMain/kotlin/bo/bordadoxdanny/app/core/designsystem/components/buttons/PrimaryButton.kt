package bo.bordadoxdanny.app.core.designsystem.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.core.designsystem.Res
import bo.bordadoxdanny.app.core.designsystem.a11y_loading_indicator
import org.jetbrains.compose.resources.stringResource

// TalkBack announces: "[Button Text] / Loading, please wait, button"
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    contentDescription: String? = null
) {
    val loadingDescription = stringResource(Res.string.a11y_loading_indicator)
    val resolvedDescription = when {
        isLoading -> loadingDescription
        else -> contentDescription ?: text
    }

    OutlinedButton(
        onClick = onClick,
        modifier = modifier.semantics {
            role = Role.Button
            this.contentDescription = resolvedDescription
            if (!enabled || isLoading) disabled()
        },
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled) AppTheme.colors.primary
                    else AppTheme.colors.textPrimary.copy(alpha = 0.1f)
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(20.dp)
                    .semantics { invisibleToUser() },
                strokeWidth = 2.dp,
                color = AppTheme.colors.primary
            )
        } else {
            Text(
                text = text,
                style = AppTheme.typography.labelLarge,
                color = if (enabled) AppTheme.colors.primary
                        else AppTheme.colors.textPrimary.copy(alpha = 0.3f),
                modifier = Modifier.semantics { invisibleToUser() }
            )
        }
    }
}
