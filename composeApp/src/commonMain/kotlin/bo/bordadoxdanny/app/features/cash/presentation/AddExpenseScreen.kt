package bo.bordadoxdanny.app.features.cash.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme

@Composable
fun AddExpenseScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Aquí se implementará la lógica de llenado de un nuevo egreso",
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.textPrimary
        )
    }
}
