package bo.bordadoxdanny.app.features.cash.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.core.designsystem.components.dividers.HorizontalDivider
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.Res
import bo.bordadoxdanny.app.a11y_cash_entry
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

// TalkBack announces: "Caja, heading"
// Each item: "Entrada de caja: [Reason], Tipo: [Type], Monto: [Amount]"
@Composable
fun CashScreen(viewModel: CashViewModel = koinViewModel()) {
    val entries by viewModel.cashEntries.collectAsState()

    Column {
        Text(
            text = "Caja", 
            style = AppTheme.typography.headlineLarge,
            color = AppTheme.colors.textPrimary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyColumn {
            items(entries) { entry ->
                HorizontalDivider()
                
                val contentDesc = stringResource(
                    Res.string.a11y_cash_entry,
                    entry.reason,
                    entry.type,
                    entry.amount.toString()
                )

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .semantics(mergeDescendants = true) {
                            contentDescription = contentDesc
                        }
                ) {
                    Text(
                        text = entry.reason, 
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.colors.textPrimary
                    )
                    Text(
                        text = "${entry.type}: Bs. ${entry.amount}", 
                        style = AppTheme.typography.labelLarge,
                        color = AppTheme.colors.textPrimary.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}
