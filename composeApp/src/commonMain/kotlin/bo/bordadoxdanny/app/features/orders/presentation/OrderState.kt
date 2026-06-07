package bo.bordadoxdanny.app.features.orders.presentation

import bo.bordadoxdanny.app.Res
import org.jetbrains.compose.resources.StringResource

sealed class OrderState {
    data object Loading : OrderState()
    data class Success(val orders: List<bo.bordadoxdanny.app.features.orders.domain.Order>) : OrderState()
    data class Error(val messageResId: StringResource) : OrderState()
}
