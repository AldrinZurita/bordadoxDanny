package bo.bordadoxdanny.app.features.orders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.Res
import bo.bordadoxdanny.app.no_data_for_period
import bo.bordadoxdanny.app.features.orders.domain.GetOrdersUseCase
import bo.bordadoxdanny.app.features.profile.domain.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class OrdersViewModel(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val authRepository: AuthRepository
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
                val user = authRepository.getCurrentUser().filterNotNull().first()
                getOrdersUseCase(user.id).collect { orders ->
                    _state.value = OrderState.Success(orders)
                }
            } catch (e: Exception) {
                _state.value = OrderState.Error(Res.string.no_data_for_period)
            }
        }
    }
}
