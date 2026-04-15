package bo.bordadoxdanny.app.features.orders.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OrdersScreen(viewModel: OrdersViewModel = koinViewModel()) {
    val orders by viewModel.orders.collectAsState()
    Column {
        Text(
            text = "Órdenes", 
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyColumn {
            items(orders) { order ->
                Divider()
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = order.description, style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Bs. ${order.amount}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}
