package bo.bordadoxdanny.app.features.orders.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.features.orders.domain.CreateOrderUseCase
import bo.bordadoxdanny.app.features.orders.domain.Order
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class CreateOrderViewModel(
    private val createOrderUseCase: CreateOrderUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<CreateOrderState>(CreateOrderState.Idle)
    val state = _state.asStateFlow()

    private val _form = MutableStateFlow(CreateOrderForm())
    val form = _form.asStateFlow()

    fun onCustomerNameChange(name: String) {
        val isValid = name.trim().length >= 3 && name.any { it in "aeiouAEIOU" }
        _form.update { it.copy(customerName = name, customerNameError = !isValid) }
    }

    fun onDescriptionChange(description: String) {
        _form.update { it.copy(description = description) }
    }

    fun onQuantityChange(q: String) {
        _form.update { it.copy(quantity = q.toIntOrNull() ?: 0) }
    }

    fun onUnitPriceChange(p: String) {
        _form.update { it.copy(unitPrice = p) }
    }

    fun onInitialPaymentChange(p: String) {
        _form.update { it.copy(initialPayment = p) }
    }

    fun createOrder() {
        val current = _form.value
        if (current.customerNameError || current.customerName.isBlank()) {
            _state.value = CreateOrderState.Error("Invalid form data")
            return
        }

        viewModelScope.launch {
            _state.value = CreateOrderState.Loading
            try {
                val order = Order(
                    customerName = current.customerName.trim(),
                    deliveryDate = current.deliveryDate ?: Clock.System.now().toEpochMilliseconds(),
                    description = current.description,
                    quantity = current.quantity,
                    unitPrice = current.unitPrice.toDoubleOrNull() ?: 0.0,
                    initialPayment = current.initialPayment.toDoubleOrNull() ?: 0.0,
                    total = current.total,
                    balance = current.balance,
                    createdAt = Clock.System.now().toEpochMilliseconds()
                )
                createOrderUseCase(order)
                _state.value = CreateOrderState.Success
            } catch (e: Exception) {
                _state.value = CreateOrderState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}
