package bo.bordadoxdanny.app.features.reports.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import bo.bordadoxdanny.app.Res
import bo.bordadoxdanny.app.all_months
import bo.bordadoxdanny.app.period
import bo.bordadoxdanny.app.core.designsystem.components.dividers.HorizontalDivider
import bo.bordadoxdanny.app.core.designsystem.components.icons.AppIcon
import bo.bordadoxdanny.app.core.designsystem.components.icons.AppIcons
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.features.reports.domain.Period
import org.jetbrains.compose.resources.stringResource

@Composable
fun PeriodSelectorDialog(
    periods: List<Period>,
    selectedPeriod: Period,
    onPeriodSelected: (Period) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.period),
                        style = AppTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        AppIcon(resource = AppIcons.Close, contentDescription = null, tint = AppTheme.colors.textPrimary)
                    }
                }
                
                HorizontalDivider()

                LazyColumn {
                    items(periods) { period ->
                        PeriodItem(
                            period = period,
                            isSelected = period == selectedPeriod,
                            onClick = { onPeriodSelected(period) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun PeriodItem(
    period: Period,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) AppTheme.colors.primary.copy(alpha = 0.1f) else AppTheme.colors.surface
    val textColor = if (isSelected) AppTheme.colors.primary else AppTheme.colors.textPrimary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = when (period) {
                Period.AllMonths -> stringResource(Res.string.all_months)
                is Period.Year -> period.year.toString()
                is Period.Month -> "${period.month}/${period.year}"
            },
            style = AppTheme.typography.bodyMedium,
            color = textColor
        )
        if (isSelected) {
            AppIcon(resource = AppIcons.Check, contentDescription = null, tint = AppTheme.colors.primary)
        }
    }
}
