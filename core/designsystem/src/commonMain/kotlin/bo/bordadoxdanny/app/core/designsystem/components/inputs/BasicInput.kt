package bo.bordadoxdanny.app.core.designsystem.components.inputs

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.core.designsystem.accessibility.A11yFormatters
import bo.bordadoxdanny.app.core.designsystem.Res
import bo.bordadoxdanny.app.core.designsystem.a11y_input_generic_filled
import org.jetbrains.compose.resources.stringResource

// TalkBack announces: "[Label], edit box" 
// When filled: "Campo [Label]: [Value], edit box"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    inputContentDescription: String? = null
) {
    val resolvedDescription = if (value.isNotBlank()) {
        stringResource(
            Res.string.a11y_input_generic_filled, 
            label, 
            A11yFormatters.sanitizeUserInput(value)
        )
    } else {
        inputContentDescription ?: label
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.semantics {
            contentDescription = resolvedDescription
        },
        enabled = enabled,
        singleLine = singleLine,
        label = { Text(label) },
        textStyle = AppTheme.typography.bodyMedium.copy(color = AppTheme.colors.textPrimary),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = AppTheme.colors.textPrimary,
            unfocusedTextColor = AppTheme.colors.textPrimary,
            disabledTextColor = AppTheme.colors.textPrimary.copy(alpha = 0.38f),
            focusedBorderColor = AppTheme.colors.primary,
            unfocusedBorderColor = AppTheme.colors.textPrimary.copy(alpha = 0.5f),
            disabledBorderColor = AppTheme.colors.textPrimary.copy(alpha = 0.12f),
            focusedLabelColor = AppTheme.colors.primary,
            unfocusedLabelColor = AppTheme.colors.textPrimary.copy(alpha = 0.6f),
            cursorColor = AppTheme.colors.primary,
            selectionColors = TextSelectionColors(
                handleColor = AppTheme.colors.primary,
                backgroundColor = AppTheme.colors.primary.copy(alpha = 0.4f)
            )
        ),
        shape = RoundedCornerShape(8.dp)
    )
}
