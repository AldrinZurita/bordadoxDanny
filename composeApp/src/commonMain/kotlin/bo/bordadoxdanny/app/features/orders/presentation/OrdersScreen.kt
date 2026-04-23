package bo.bordadoxdanny.app.features.orders.presentation

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
import bo.bordadoxdanny.app.a11y_order_card
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

// TalkBack announces: "Órdenes, heading" 
// Each list item: "Orden: [Description], Monto: [Amount]"
@Composable
fun OrdersScreen(viewModel: OrdersViewModel = koinViewModel()) {
    val orders by viewModel.orders.collectAsState()

    Column {
        Text(
            text = "Órdenes", 
            style = AppTheme.typography.headlineLarge,
            color = AppTheme.colors.textPrimary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyColumn {
            items(orders) { order ->
                HorizontalDivider() 

                val contentDesc = stringResource(
                    Res.string.a11y_order_card,
                    order.description,
                    order.amount.toString()
                )

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .semantics(mergeDescendants = true) {
                            contentDescription = contentDesc
                        }
                ) {
                    Text(
                        text = order.description, 
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.colors.textPrimary
                    )
                    Text(
                        text = "Bs. ${order.amount}", 
                        style = AppTheme.typography.labelLarge,
                        color = AppTheme.colors.textPrimary.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}
