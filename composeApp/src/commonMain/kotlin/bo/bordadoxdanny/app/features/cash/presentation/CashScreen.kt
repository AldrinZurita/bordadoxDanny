package bo.bordadoxdanny.app.features.cash.presentation

import org.jetbrains.compose.resources.DrawableResource
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.Res
import bo.bordadoxdanny.app.*
import bo.bordadoxdanny.app.core.designsystem.calendar_blank

import bo.bordadoxdanny.app.core.designsystem.Res as DsRes
import bo.bordadoxdanny.app.core.designsystem.components.dividers.HorizontalDivider
import bo.bordadoxdanny.app.core.designsystem.components.icons.AppIcon
import bo.bordadoxdanny.app.core.designsystem.flecha_abajo
import bo.bordadoxdanny.app.core.designsystem.flecha_arriba
import bo.bordadoxdanny.app.core.designsystem.ic_plus
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.features.cash.domain.Transaction
import bo.bordadoxdanny.app.features.cash.domain.TransactionType
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CashScreen(
    viewModel: CashViewModel = koinViewModel(),
    onAddIncome: () -> Unit,
    onAddExpense: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        containerColor = AppTheme.colors.background,
        floatingActionButton = {
            CashFAB(
                type = state.selectedType,
                onClick = {
                    if (state.selectedType == TransactionType.INCOME) onAddIncome() else onAddExpense()
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CashTabs(
                selectedType = state.selectedType,
                onTypeSelected = { viewModel.selectType(it) }
            )

            DateHeader()
            HorizontalDivider()

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(state.transactions) { transaction ->
                    TransactionItem(transaction)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }

            TotalSummary(
                type = state.selectedType,
                amount = state.totalAmount
            )
        }
    }
}

@Composable
private fun CashTabs(selectedType: TransactionType, onTypeSelected: (TransactionType) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(AppTheme.colors.surfaceVariant)
            .padding(4.dp)
    ) {
        TabItem(
            modifier = Modifier.weight(1f),
            title = stringResource(Res.string.income),
            icon = DsRes.drawable.flecha_arriba,
            isSelected = selectedType == TransactionType.INCOME,
            onClick = { onTypeSelected(TransactionType.INCOME) }
        )
        TabItem(
            modifier = Modifier.weight(1f),
            title = stringResource(Res.string.expenses),
            icon = DsRes.drawable.flecha_abajo,
            isSelected = selectedType == TransactionType.EXPENSE,
            onClick = { onTypeSelected(TransactionType.EXPENSE) }
        )
    }
}

@Composable
private fun TabItem(modifier: Modifier, title: String, icon: DrawableResource, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) AppTheme.colors.primary.copy(alpha = 0.1f) else AppTheme.colors.surfaceVariant)
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AppIcon(
                resource = icon,
                tint = if (isSelected) AppTheme.colors.primary else AppTheme.colors.textSecondary,
                modifier = Modifier.size(16.dp),
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text(text = title, style = AppTheme.typography.titleSmall, color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.textSecondary)
        }
    }
}

@Composable
private fun DateHeader() {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(AppTheme.colors.surfaceVariant)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // CORRECCIÓN: Iconos reales de calendario y flecha
        AppIcon(DsRes.drawable.calendar_blank, tint = AppTheme.colors.primary, modifier = Modifier.size(16.dp), contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text(text = "June 2026", style = AppTheme.typography.labelLarge, color = AppTheme.colors.primary)
        Spacer(Modifier.width(4.dp))
        AppIcon(DsRes.drawable.flecha_abajo, tint = AppTheme.colors.primary, modifier = Modifier.size(16.dp), contentDescription = null)
    }
}

@Composable
private fun TransactionItem(transaction: Transaction) {
    val date = Instant.fromEpochMilliseconds(transaction.timestamp).toLocalDateTime(TimeZone.currentSystemDefault())
    val dateStr = "${date.dayOfMonth.toString().padStart(2, '0')}/${date.monthNumber.toString().padStart(2, '0')}"
    val timeStr = "${date.hour.toString().padStart(2, '0')}:${date.minute.toString().padStart(2, '0')}"

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = "$dateStr - $timeStr", style = AppTheme.typography.labelSmall, color = AppTheme.colors.textSecondary)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = transaction.reference, style = AppTheme.typography.titleMedium, color = AppTheme.colors.textPrimary)
                Text(text = transaction.description, style = AppTheme.typography.bodyMedium, color = AppTheme.colors.textSecondary)
            }
            val color = if (transaction.type == TransactionType.INCOME) AppTheme.colors.success else AppTheme.colors.error
            val prefix = if (transaction.type == TransactionType.INCOME) "+" else "-"
            Text(
                text = "$prefix${transaction.amount.toInt()} ${stringResource(Res.string.currency_bs)}",
                style = AppTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = color
            )
        }
    }
}

@Composable
private fun TotalSummary(type: TransactionType, amount: Double) {
    val label = if (type == TransactionType.INCOME) Res.string.total_income_month else Res.string.total_expenses_month
    val color = if (type == TransactionType.INCOME) AppTheme.colors.success else AppTheme.colors.error
    val prefix = if (type == TransactionType.INCOME) "+" else "-"

    Row(modifier = Modifier.fillMaxWidth().background(AppTheme.colors.surface).padding(24.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(label), style = AppTheme.typography.bodyMedium, color = AppTheme.colors.textSecondary)
        Text(text = "$prefix${amount.toInt()} ${stringResource(Res.string.currency_bs)}", style = AppTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold), color = color)
    }
}

@Composable
private fun CashFAB(type: TransactionType, onClick: () -> Unit) {
    val color = if (type == TransactionType.INCOME) AppTheme.colors.success else AppTheme.colors.error
    Box(
        modifier = Modifier.size(56.dp).clip(CircleShape).background(color).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // CORRECCIÓN: Uso de tu icono real ic_plus
        AppIcon(
            resource = DsRes.drawable.ic_plus,
            tint = AppTheme.colors.background,
            modifier = Modifier.size(24.dp),
            contentDescription = "Agregar Transacción"
        )
    }
}