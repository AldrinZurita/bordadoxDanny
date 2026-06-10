package bo.bordadoxdanny.app.features.cash.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.Res
import bo.bordadoxdanny.app.*
import bo.bordadoxdanny.app.core.designsystem.*
//import bo.bordadoxdanny.app.generated.resources.* // Import de los autogenerados de App
import bo.bordadoxdanny.app.core.designsystem.Res as DsRes
import bo.bordadoxdanny.app.core.designsystem.components.dividers.HorizontalDivider
import bo.bordadoxdanny.app.core.designsystem.components.icons.AppIcon
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.features.cash.domain.Transaction
import bo.bordadoxdanny.app.features.cash.domain.TransactionType
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashScreen(
    viewModel: CashViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        containerColor = AppTheme.colors.background,
        floatingActionButton = {
            CashFAB(
                type = state.selectedType,
                onClick = {
                    if (state.selectedType == TransactionType.INCOME) viewModel.showAddIncome(true)
                    else viewModel.showAddExpense(true)
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            CashTabs(
                selectedType = state.selectedType,
                onTypeSelected = { viewModel.selectType(it) }
            )

            MonthFilterHeader(
                selectedMonth = state.selectedMonth,
                availableMonths = state.availableMonths,
                expanded = state.showMonthSelector,
                onToggle = { viewModel.toggleMonthSelector(it) },
                onMonthSelected = { viewModel.selectMonth(it) }
            )

            HorizontalDivider()

            Box(modifier = Modifier.weight(1f)) {
                if (state.transactions.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No hay transacciones", // Placeholder si no tienes el string
                            style = AppTheme.typography.bodyMedium,
                            color = AppTheme.colors.textSecondary
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.transactions) { transaction ->
                            TransactionItem(transaction)
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                        }
                    }
                }
            }

            TotalSummary(type = state.selectedType, amount = state.totalAmount)
        }

        // Bottom Sheets
        if (state.showAddIncomeSheet) {
            AddIncomeSheet(
                onDismiss = { viewModel.showAddIncome(false) },
                onSave = { amount, detail -> viewModel.saveTransaction(amount, detail, TransactionType.INCOME) }
            )
        }

        if (state.showAddExpenseSheet) {
            AddExpenseSheet(
                onDismiss = { viewModel.showAddExpense(false) },
                onSave = { amount, detail, cat -> viewModel.saveTransaction(amount, detail, TransactionType.EXPENSE, cat) }
            )
        }
    }
}

@Composable
private fun MonthFilterHeader(
    selectedMonth: String?,
    availableMonths: List<String>,
    expanded: Boolean,
    onToggle: (Boolean) -> Unit,
    onMonthSelected: (String?) -> Unit
) {
    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(AppTheme.colors.surfaceVariant)
                .clickable { onToggle(true) }
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppIcon(DsRes.drawable.calendar_blank, tint = AppTheme.colors.primary, modifier = Modifier.size(16.dp), contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(
                text = selectedMonth ?: stringResource(Res.string.all_months),
                style = AppTheme.typography.labelLarge,
                color = AppTheme.colors.primary
            )
            Spacer(Modifier.width(4.dp))
            AppIcon(DsRes.drawable.flecha_abajo, tint = AppTheme.colors.primary, modifier = Modifier.size(16.dp), contentDescription = null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onToggle(false) },
            modifier = Modifier.background(AppTheme.colors.surface)
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.all_months), style = AppTheme.typography.bodyMedium) },
                onClick = { onMonthSelected(null) }
            )
            availableMonths.forEach { month ->
                DropdownMenuItem(
                    text = { Text(month, style = AppTheme.typography.bodyMedium) },
                    onClick = { onMonthSelected(month) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddIncomeSheet(onDismiss: () -> Unit, onSave: (Double, String) -> Unit) {
    var amount by remember { mutableStateOf("") }
    var detail by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = AppTheme.colors.surface,
        dragHandle = { BottomSheetDefaults.DragHandle(color = AppTheme.colors.divider) }
    ) {
        Column(modifier = Modifier.padding(24.dp).padding(bottom = 32.dp)) {
            Text(stringResource(Res.string.add_income), style = AppTheme.typography.titleMedium, color = AppTheme.colors.textPrimary)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text(stringResource(Res.string.amount)) },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = detail,
                onValueChange = { detail = it },
                label = { Text(stringResource(Res.string.detail)) },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors()
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { onSave(amount.toDoubleOrNull() ?: 0.0, detail) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.success),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(stringResource(Res.string.save), color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddExpenseSheet(onDismiss: () -> Unit, onSave: (Double, String, String) -> Unit) {
    var amount by remember { mutableStateOf("") }
    var detail by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("cat_material") }

    val categories = listOf(
        "cat_material", "cat_threads", "cat_machinery", "cat_maintenance",
        "cat_services", "cat_rent", "cat_personnel", "cat_transport", "cat_others"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = AppTheme.colors.surface
    ) {
        Column(modifier = Modifier.padding(24.dp).padding(bottom = 32.dp)) {
            Text(stringResource(Res.string.new_expense), style = AppTheme.typography.titleMedium, color = AppTheme.colors.textPrimary)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text(stringResource(Res.string.amount)) },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors()
            )
            Spacer(Modifier.height(16.dp))
            Text(stringResource(Res.string.category), style = AppTheme.typography.labelLarge, color = AppTheme.colors.textSecondary)
            Spacer(Modifier.height(8.dp))

            LazyVerticalGrid(columns = GridCells.Fixed(3), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.height(150.dp)) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) AppTheme.colors.primary.copy(0.1f) else AppTheme.colors.surfaceVariant)
                            .border(1.dp, if (isSelected) AppTheme.colors.primary else Color.Transparent, RoundedCornerShape(8.dp))
                            .clickable { selectedCategory = cat }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = getCategoryString(cat),
                            style = AppTheme.typography.labelSmall,
                            color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.textPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = detail,
                onValueChange = { detail = it },
                label = { Text(stringResource(Res.string.detail)) },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors()
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { onSave(amount.toDoubleOrNull() ?: 0.0, detail, selectedCategory) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.error),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(stringResource(Res.string.save), color = Color.White)
            }
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction) {
    val date = Instant.fromEpochMilliseconds(transaction.timestamp).toLocalDateTime(TimeZone.currentSystemDefault())
    // CORRECCIÓN SINTÁCTICA: padStart usa Char ('0') no String escapado
    val dateStr = "${date.dayOfMonth.toString().padStart(2, '0')}/${date.monthNumber.toString().padStart(2, '0')}"
    val timeStr = "${date.hour.toString().padStart(2, '0')}:${date.minute.toString().padStart(2, '0')}"

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = "$dateStr - $timeStr", style = AppTheme.typography.labelSmall, color = AppTheme.colors.textSecondary)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                val reference = if (transaction.type == TransactionType.INCOME && transaction.reference.isEmpty()) {
                    stringResource(Res.string.manual_income)
                } else transaction.reference

                if (reference.isNotEmpty()) {
                    Text(text = reference, style = AppTheme.typography.titleMedium, color = AppTheme.colors.textPrimary)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (transaction.type == TransactionType.EXPENSE && transaction.category != null) {
                        Text(
                            text = getCategoryString(transaction.category).uppercase(),
                            style = AppTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = AppTheme.colors.primary
                        )
                        Text(" • ", color = AppTheme.colors.textSecondary)
                    }
                    Text(text = transaction.description, style = AppTheme.typography.bodyMedium, color = AppTheme.colors.textSecondary)
                }
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
private fun TabItem(modifier: Modifier, title: String, icon: org.jetbrains.compose.resources.DrawableResource, isSelected: Boolean, onClick: () -> Unit) {
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
            Text(
                text = title,
                style = AppTheme.typography.titleSmall,
                color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.textSecondary
            )
        }
    }
}

@Composable
private fun TotalSummary(type: TransactionType, amount: Double) {
    val label = if (type == TransactionType.INCOME) Res.string.total_income_month else Res.string.total_expenses_month
    val color = if (type == TransactionType.INCOME) AppTheme.colors.success else AppTheme.colors.error
    val prefix = if (type == TransactionType.INCOME) "+" else "-"

    Row(
        modifier = Modifier.fillMaxWidth().background(AppTheme.colors.surface).padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(label), style = AppTheme.typography.bodyMedium, color = AppTheme.colors.textSecondary)
        Text(text = "$prefix${amount.toInt()} ${stringResource(Res.string.currency_bs)}", style = AppTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = color)
    }
}

@Composable
private fun CashFAB(type: TransactionType, onClick: () -> Unit) {
    val color = if (type == TransactionType.INCOME) AppTheme.colors.success else AppTheme.colors.error
    Box(
        modifier = Modifier.size(56.dp).clip(CircleShape).background(color).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        AppIcon(
            resource = DsRes.drawable.ic_plus,
            tint = AppTheme.colors.background,
            modifier = Modifier.size(24.dp),
            contentDescription = null
        )
    }
}

@Composable
private fun getCategoryString(key: String): String = when(key) {
    "cat_material" -> stringResource(Res.string.cat_material)
    "cat_threads" -> stringResource(Res.string.cat_threads)
    "cat_machinery" -> stringResource(Res.string.cat_machinery)
    "cat_maintenance" -> stringResource(Res.string.cat_maintenance)
    "cat_services" -> stringResource(Res.string.cat_services)
    "cat_rent" -> stringResource(Res.string.cat_rent)
    "cat_personnel" -> stringResource(Res.string.cat_personnel)
    "cat_transport" -> stringResource(Res.string.cat_transport)
    else -> stringResource(Res.string.cat_others)
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AppTheme.colors.primary,
    unfocusedBorderColor = AppTheme.colors.divider,
    focusedLabelColor = AppTheme.colors.primary,
    cursorColor = AppTheme.colors.primary,
    focusedTextColor = AppTheme.colors.textPrimary,
    unfocusedTextColor = AppTheme.colors.textPrimary
)