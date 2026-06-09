package bo.bordadoxdanny.app.features.orders.presentation.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.core.designsystem.components.buttons.PrimaryButton
import bo.bordadoxdanny.app.core.designsystem.components.inputs.BasicInput
import bo.bordadoxdanny.app.core.designsystem.components.dividers.HorizontalDivider
import bo.bordadoxdanny.app.core.designsystem.components.CurrencyInput
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrderScreen(
    onNavigateBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: CreateOrderViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val form by viewModel.form.collectAsState()

    LaunchedEffect(state) {
        if (state is CreateOrderState.Success) {
            onSuccess()
        }
    }

    Scaffold(
        containerColor = AppTheme.colors.background,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Nueva Orden", 
                        style = AppTheme.typography.headlineLarge,
                        color = AppTheme.colors.textPrimary
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text(
                            text = "<", 
                            style = AppTheme.typography.headlineLarge, 
                            color = AppTheme.colors.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colors.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // SECCIÓN: DATOS DEL CLIENTE
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                BasicInput(
                    value = form.customerName,
                    onValueChange = viewModel::onCustomerNameChange,
                    label = "Nombre del Cliente",
                    modifier = Modifier.fillMaxWidth()
                )
                if (form.customerNameError) {
                    Text(
                        text = "Nombre inválido (min 3 chars, vocales y consonantes)", 
                        style = AppTheme.typography.labelLarge, 
                        color = AppTheme.colors.primary
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp
            )

            // SECCIÓN: DETALLE DE PEDIDO
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                BasicInput(
                    value = form.description,
                    onValueChange = viewModel::onDescriptionChange,
                    label = "Diseño / Descripción",
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BasicInput(
                        value = if (form.quantity == 0) "" else form.quantity.toString(),
                        onValueChange = viewModel::onQuantityChange,
                        label = "Cant.",
                        modifier = Modifier.weight(1f)
                    )
                    
                    CurrencyInput(
                        value = form.unitPrice,
                        onValueChange = viewModel::onUnitPriceChange,
                        label = "P. Unit",
                        currencySymbol = "Bs",
                        modifier = Modifier.weight(1.5f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TOTAL:", 
                        style = AppTheme.typography.labelLarge, 
                        color = AppTheme.colors.textPrimary
                    )
                    Text(
                        text = "Bs ${form.total}", 
                        style = AppTheme.typography.bodyMedium, 
                        color = AppTheme.colors.primary
                    )
                }
            }

            // SECCIÓN: PAGO Y SALDO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.surface, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    CurrencyInput(
                        value = form.initialPayment,
                        onValueChange = viewModel::onInitialPaymentChange,
                        label = "Pago Inicial (Adelanto)",
                        currencySymbol = "Bs",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "SALDO:", 
                            style = AppTheme.typography.labelLarge, 
                            color = AppTheme.colors.textPrimary
                        )
                        Text(
                            text = "Bs ${form.balance}", 
                            style = AppTheme.typography.bodyMedium, 
                            color = AppTheme.colors.primary
                        )
                    }
                }
            }

            PrimaryButton(
                text = "CREAR ORDEN",
                onClick = viewModel::createOrder,
                modifier = Modifier.fillMaxWidth(),
                isLoading = state is CreateOrderState.Loading
            )

            if (state is CreateOrderState.Error) {
                Text(
                    text = (state as CreateOrderState.Error).message,
                    color = AppTheme.colors.primary,
                    style = AppTheme.typography.labelLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
