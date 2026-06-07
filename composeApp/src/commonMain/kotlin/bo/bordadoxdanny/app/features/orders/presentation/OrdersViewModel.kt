package bo.bordadoxdanny.app.features.orders.presentation

import bo.bordadoxdanny.app.Res
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.features.orders.domain.GetOrdersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrdersViewModel(
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<OrderState>(OrderState.Loading)
    val state: StateFlow<OrderState> = _state.asStateFlow()

    init {
        onIntent(OrderIntent.OnLoadOrders)
    }

    fun onIntent(intent: OrderIntent) {
        when (intent) {
            is OrderIntent.OnLoadOrders -> loadOrders()
            is OrderIntent.OnRefresh -> loadOrders()
            OrderIntent.OnNavigateToCreateOrder -> {}
            is OrderIntent.OnOrderClick -> {}
        }
    }

    private fun loadOrders() {
        viewModelScope.launch {
            _state.value = OrderState.Loading
            try {
                getOrdersUseCase().collect { orders ->
                    _state.value = OrderState.Success(orders)
                }
            } catch (e: Exception) {
                _state.value = OrderState.Error(Res.string.no_data_for_period)
            }
        }
    }
}
