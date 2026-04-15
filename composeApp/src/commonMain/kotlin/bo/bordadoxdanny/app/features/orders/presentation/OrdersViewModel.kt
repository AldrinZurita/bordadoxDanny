package bo.bordadoxdanny.app.features.orders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.features.orders.domain.Order
import bo.bordadoxdanny.app.features.orders.domain.GetOrdersUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class OrdersViewModel(getOrdersUseCase: GetOrdersUseCase) : ViewModel() {
    val orders: StateFlow<List<Order>> = getOrdersUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
