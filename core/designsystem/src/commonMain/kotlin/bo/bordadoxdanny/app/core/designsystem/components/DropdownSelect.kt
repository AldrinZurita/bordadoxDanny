package bo.bordadoxdanny.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.core.designsystem.util.Preview

/**
 * Selector desplegable para estados de órdenes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelect(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
            enabled = enabled,
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

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(AppTheme.colors.surface)
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = selectionOption,
                            style = AppTheme.typography.bodyMedium,
                            color = AppTheme.colors.textPrimary
                        )
                    },
                    onClick = {
                        onOptionSelected(selectionOption)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Preview
@Composable
fun DropdownSelectPreview() {
    DropdownSelect(
        options = listOf("Pendiente", "En proceso", "Entregado"),
        selectedOption = "Pendiente",
        onOptionSelected = {},
        label = "Estado de la orden"
    )
}
