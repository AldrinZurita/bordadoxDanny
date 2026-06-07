package bo.bordadoxdanny.app.features.orders.presentation

import bo.bordadoxdanny.app.features.orders.domain.Order

sealed class OrderIntent {
    data object OnLoadOrders : OrderIntent()
    data object OnRefresh : OrderIntent()
    data object OnNavigateToCreateOrder : OrderIntent()
    data class OnOrderClick(val order: Order) : OrderIntent()
}
