package bo.bordadoxdanny.app.features.orders.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.core.designsystem.components.dividers.HorizontalDivider
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OrdersScreen(
    onAddOrder: () -> Unit,
    viewModel: OrdersViewModel = koinViewModel()
) {
    val orders by viewModel.orders.collectAsState()

    Scaffold(
        containerColor = AppTheme.colors.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddOrder,
                containerColor = AppTheme.colors.primary,
                contentColor = AppTheme.colors.background
            ) {
                Text("+", style = AppTheme.typography.headlineLarge)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text(
                text = "Orders",
                style = AppTheme.typography.headlineLarge,
                color = AppTheme.colors.textPrimary,
                modifier = Modifier.padding(16.dp)
            )
            
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(orders) { order ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "#${order.id} ${order.customerName}",
                            style = AppTheme.typography.labelLarge,
                            color = AppTheme.colors.textPrimary
                        )
                        Text(
                            text = order.description,
                            style = AppTheme.typography.bodyMedium,
                            color = AppTheme.colors.textPrimary.copy(alpha = 0.7f)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total: Bs ${order.total}",
                                style = AppTheme.typography.bodyMedium,
                                color = AppTheme.colors.primary
                            )
                            if (order.balance > 0) {
                                Text(
                                    text = "Debe: Bs ${order.balance}",
                                    style = AppTheme.typography.labelLarge,
                                    color = AppTheme.colors.primary
                                )
                            }
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}
