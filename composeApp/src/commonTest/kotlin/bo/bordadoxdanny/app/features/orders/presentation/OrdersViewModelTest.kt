package bo.bordadoxdanny.app.features.orders.presentation

import bo.bordadoxdanny.app.features.orders.domain.Order
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class OrdersViewModelTest {

    @Test
    fun `given OnLoadOrders intent, state transitions to Success with orders`() = runTest {
        val orders = listOf(
            Order(id = 1, customerName = "Juan Perez", deliveryDate = 0, description = "Camisa", quantity = 1, unitPrice = 100.0, initialPayment = 0.0, total = 100.0, balance = 0.0),
            Order(id = 2, customerName = "Maria Lopez", deliveryDate = 0, description = "Pantalon", quantity = 2, unitPrice = 50.0, initialPayment = 0.0, total = 100.0, balance = 0.0)
        )
        val fakeGetOrdersUseCase = { flowOf(orders) }
        val vm = OrdersViewModel(fakeGetOrdersUseCase)

        assertTrue(vm.state.value is OrderState.Success)
        val success = vm.state.value as OrderState.Success
        assertEquals(2, success.orders.size)
    }

    @Test
    fun `given empty orders list, state is Success with empty list`() = runTest {
        val fakeGetOrdersUseCase = { flowOf(emptyList()) }
        val vm = OrdersViewModel(fakeGetOrdersUseCase)

        assertTrue(vm.state.value is OrderState.Success)
        val success = vm.state.value as OrderState.Success
        assertTrue(success.orders.isEmpty())
    }

    @Test
    fun `given OnRefresh intent, reloads orders`() = runTest {
        val orders = listOf(
            Order(id = 1, customerName = "Test", deliveryDate = 0, description = "Test", quantity = 1, unitPrice = 10.0, initialPayment = 0.0, total = 10.0, balance = 0.0)
        )
        val fakeGetOrdersUseCase = { flowOf(orders) }
        val vm = OrdersViewModel(fakeGetOrdersUseCase)

        vm.onIntent(OrderIntent.OnRefresh)

        assertTrue(vm.state.value is OrderState.Success)
        val success = vm.state.value as OrderState.Success
        assertEquals(1, success.orders.size)
    }
}
