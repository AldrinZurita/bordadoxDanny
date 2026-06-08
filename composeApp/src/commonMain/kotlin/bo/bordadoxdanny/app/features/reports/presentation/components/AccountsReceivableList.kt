package bo.bordadoxdanny.app.features.reports.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.Res
import bo.bordadoxdanny.app.accounts_receivable_empty
import bo.bordadoxdanny.app.core.designsystem.components.dividers.HorizontalDivider
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.features.reports.domain.AccountsReceivableItem
import org.jetbrains.compose.resources.stringResource

@Composable
fun AccountsReceivableList(
    items: List<AccountsReceivableItem>,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) {
        Text(
            text = stringResource(Res.string.accounts_receivable_empty),
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.textSecondary,
            modifier = modifier.padding(16.dp)
        )
    } else {
        Column(modifier = modifier) {
            items.forEachIndexed { index, item ->
                AccountsReceivableRow(item)
                if (index < items.size - 1) {
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Composable
private fun AccountsReceivableRow(item: AccountsReceivableItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    color = if (item.isUrgent) Color(0xFF4CAF50) else Color.Gray,
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "#${item.id} · ${item.clientName} · ${item.description}",
            style = AppTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "${item.amount} Bs",
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.error
        )
    }
}
