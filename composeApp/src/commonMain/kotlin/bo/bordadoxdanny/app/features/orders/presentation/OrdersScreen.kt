package bo.bordadoxdanny.app.features.orders.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.*
import bo.bordadoxdanny.app.core.designsystem.components.buttons.PrimaryButton
import bo.bordadoxdanny.app.core.designsystem.components.dividers.HorizontalDivider
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OrdersScreen(
    onAddOrder: () -> Unit,
    viewModel: OrdersViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(AppTheme.colors.background)) {
        when (val currentState = state) {
            is OrderState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = AppTheme.colors.primary
                )
            }
            is OrderState.Success -> {
                OrdersContent(
                    orders = currentState.orders,
                    onAddOrder = onAddOrder,
                    onIntent = viewModel::onIntent
                )
            }
            is OrderState.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(currentState.messageResId),
                        color = AppTheme.colors.error,
                        style = AppTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PrimaryButton(
                        text = stringResource(Res.string.sign_in),
                        onClick = { viewModel.onIntent(OrderIntent.OnRefresh) },
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun OrdersContent(
    orders: List<bo.bordadoxdanny.app.features.orders.domain.Order>,
    onAddOrder: () -> Unit,
    onIntent: (OrderIntent) -> Unit
) {
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
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            item {
                Text(
                    text = stringResource(Res.string.nav_orders),
                    style = AppTheme.typography.headlineLarge,
                    color = AppTheme.colors.textPrimary,
                    modifier = Modifier.padding(16.dp)
                )
            }
            if (orders.isEmpty()) {
                item {
                    Text(
                        text = stringResource(Res.string.no_data_for_period),
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.colors.textSecondary,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
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
