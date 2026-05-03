package bo.bordadoxdanny.app.core.designsystem.components.inputs

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme

@Composable
fun BasicInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = modifier,
        enabled = enabled,
        singleLine = singleLine,
        textStyle = AppTheme.typography.bodyMedium.copy(color = AppTheme.colors.textPrimary),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppTheme.colors.primary,
            focusedLabelColor = AppTheme.colors.primary,
            unfocusedBorderColor = AppTheme.colors.textPrimary.copy(alpha = 0.5f),
            cursorColor = AppTheme.colors.primary,
            selectionColors = TextSelectionColors(
                handleColor = AppTheme.colors.primary,
                backgroundColor = AppTheme.colors.primary.copy(alpha = 0.4f)
            )
        )
    )
}
