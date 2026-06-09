package bo.bordadoxdanny.app.core.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.core.designsystem.util.Preview

/**
 * Input numérico con símbolo de moneda dinámico y teclado especializado.
 */
@Composable
fun CurrencyInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    currencySymbol: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(Regex("""^\d*\.?\d*$"""))) {
                    onValueChange(newValue)
                }
            },
            label = { Text(text = label) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            isError = isError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            leadingIcon = {
                Text(
                    text = currencySymbol,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.textSecondary,
                    modifier = Modifier.padding(start = 12.dp)
                )
            },
            textStyle = AppTheme.typography.bodyMedium.copy(color = AppTheme.colors.textPrimary),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppTheme.colors.primary,
                focusedLabelColor = AppTheme.colors.primary,
                unfocusedBorderColor = AppTheme.colors.textPrimary.copy(alpha = 0.5f),
                cursorColor = AppTheme.colors.primary,
                errorBorderColor = AppTheme.colors.error,
                errorLabelColor = AppTheme.colors.error,
                errorCursorColor = AppTheme.colors.error,
                selectionColors = TextSelectionColors(
                    handleColor = AppTheme.colors.primary,
                    backgroundColor = AppTheme.colors.primary.copy(alpha = 0.4f)
                )
            )
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = AppTheme.colors.error,
                style = AppTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Preview
@Composable
fun CurrencyInputPreview() {
    CurrencyInput(
        value = "150.50",
        onValueChange = {},
        label = "Monto pagado",
        currencySymbol = "Bs."
    )
}