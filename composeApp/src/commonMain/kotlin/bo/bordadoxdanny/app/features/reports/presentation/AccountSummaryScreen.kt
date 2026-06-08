package bo.bordadoxdanny.app.features.reports.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.Res
import bo.bordadoxdanny.app.account_summary
import bo.bordadoxdanny.app.accounts_receivable
import bo.bordadoxdanny.app.all_months
import bo.bordadoxdanny.app.core.designsystem.components.dividers.HorizontalDivider
import bo.bordadoxdanny.app.core.designsystem.components.icons.AppIcon
import bo.bordadoxdanny.app.core.designsystem.components.icons.AppIcons
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.features.reports.domain.FinancialSummary
import bo.bordadoxdanny.app.features.reports.domain.Period
import bo.bordadoxdanny.app.features.reports.presentation.components.AccountsReceivableList
import bo.bordadoxdanny.app.features.reports.presentation.components.IncomeExpensesBarChart
import bo.bordadoxdanny.app.income_vs_expenses
import bo.bordadoxdanny.app.net_profit
import bo.bordadoxdanny.app.total_expenses
import bo.bordadoxdanny.app.total_income
import bo.bordadoxdanny.app.total_pending
import org.jetbrains.compose.resources.stringResource

@Composable
fun AccountSummaryScreen(
    viewModel: ReportViewModel
) {
    val state by viewModel.state.collectAsState()
    val showPeriodSelector by viewModel.showPeriodSelector.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(AppTheme.colors.background)) {
        when (val currentState = state) {
            is ReportState.Loading -> {
                // Loading indicator if needed
            }
            is ReportState.Success -> {
                AccountSummaryContent(
                    state = currentState,
                    onIntent = viewModel::onIntent
                )
            }
            is ReportState.Error -> {
                Text(
                    text = stringResource(currentState.messageResId),
                    color = AppTheme.colors.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        if (showPeriodSelector && state is ReportState.Success) {
            PeriodSelectorDialog(
                periods = (state as ReportState.Success).periods,
                selectedPeriod = (state as ReportState.Success).selectedPeriod,
                onPeriodSelected = { viewModel.onIntent(ReportIntent.OnPeriodSelected(it)) },
                onDismiss = { viewModel.onIntent(ReportIntent.OnClosePeriodSelector) }
            )
        }
    }
}

@Composable
private fun AccountSummaryContent(
    state: ReportState.Success,
    onIntent: (ReportIntent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(Res.string.account_summary),
                style = AppTheme.typography.headlineLarge,
                color = AppTheme.colors.textPrimary
            )
        }

        item {
            PeriodChip(
                period = state.selectedPeriod,
                onClick = { onIntent(ReportIntent.OnOpenPeriodSelector) }
            )
        }

        item {
            HorizontalDivider()
        }

        item {
            MetricsGrid(summary = state.summary)
        }

        item {
            SummaryBanner(summary = state.summary, period = state.selectedPeriod)
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(Res.string.income_vs_expenses),
                        style = AppTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    IncomeExpensesBarChart(data = state.mockChartData)
                }
            }
        }

        item {
            Column {
                Text(
                    text = stringResource(Res.string.accounts_receivable),
                    style = AppTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(Res.string.total_pending, state.summary.accountsReceivable.toString()),
                    style = AppTheme.typography.labelSmall,
                    color = AppTheme.colors.textSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                AccountsReceivableList(items = state.arItems)
            }
        }
    }
}

@Composable
private fun PeriodChip(period: Period, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .border(
                BorderStroke(1.dp, AppTheme.colors.primary),
                shape = RoundedCornerShape(20.dp)
            )
            .background(AppTheme.colors.surface, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AppIcon(resource = AppIcons.Calendar, contentDescription = null, modifier = Modifier.size(20.dp), tint = AppTheme.colors.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = when(period) {
                    Period.AllMonths -> stringResource(Res.string.all_months)
                    is Period.Year -> period.year.toString()
                    is Period.Month -> "${period.month}/${period.year}" // Simplified for now
                },
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.textPrimary
            )
            Spacer(modifier = Modifier.width(4.dp))
            AppIcon(resource = AppIcons.ExpandMore, contentDescription = null, modifier = Modifier.size(16.dp), tint = AppTheme.colors.textSecondary)
        }
    }
}

@Composable
private fun MetricsGrid(summary: FinancialSummary) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MetricCard(
                label = stringResource(Res.string.total_income),
                value = "${summary.totalIncome} Bs",
                icon = AppIcons.TrendingUp,
                valueColor = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                label = stringResource(Res.string.total_expenses),
                value = "${summary.totalExpenses} Bs",
                icon = AppIcons.TrendingDown,
                valueColor = AppTheme.colors.error,
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MetricCard(
                label = stringResource(Res.string.net_profit),
                value = "${summary.netProfit} Bs",
                icon = AppIcons.AttachMoney,
                valueColor = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                label = stringResource(Res.string.accounts_receivable),
                value = "${summary.accountsReceivable} Bs",
                icon = AppIcons.Info,
                valueColor = Color(0xFFFFC107),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MetricCard(
    label: String,
    value: String,
    icon: org.jetbrains.compose.resources.DrawableResource,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            AppIcon(resource = icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = valueColor)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, style = AppTheme.typography.bodyMedium, color = AppTheme.colors.textSecondary)
            Text(text = value, style = AppTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = valueColor)
        }
    }
}

@Composable
private fun SummaryBanner(summary: FinancialSummary, period: Period) {
    val periodLabel = when(period) {
        Period.AllMonths -> stringResource(Res.string.all_months)
        is Period.Year -> period.year.toString()
        is Period.Month -> "${period.month}/${period.year}"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFF4CAF50), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AppIcon(resource = AppIcons.TrendingUp, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "${stringResource(Res.string.net_profit)} · $periodLabel",
                    style = AppTheme.typography.bodyMedium
                )
                Text(
                    text = "+${summary.netProfit} Bs",
                    style = AppTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}
