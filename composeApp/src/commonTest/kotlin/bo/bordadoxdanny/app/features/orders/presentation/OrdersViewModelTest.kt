package bo.bordadoxdanny.app.features.orders.presentation

import app.cash.turbine.test
import bo.bordadoxdanny.app.features.orders.domain.GetOrdersUseCase
import bo.bordadoxdanny.app.features.orders.domain.Order
import bo.bordadoxdanny.app.features.orders.domain.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class OrdersViewModelTest {

    private lateinit var fakeRepository: FakeOrderRepository
    private lateinit var getOrdersUseCase: GetOrdersUseCase
    private lateinit var vm: OrdersViewModel

    @BeforeTest
    fun setup() {
        fakeRepository = FakeOrderRepository()
        getOrdersUseCase = GetOrdersUseCase(fakeRepository)
        vm = OrdersViewModel(getOrdersUseCase)
    }

    @Test
    fun `given OnLoadOrders intent, state transitions to Success with orders`() = runTest {
        val orders = listOf(
            Order(id = 1, customerName = "Juan Perez", deliveryDate = 0, description = "Camisa", quantity = 1, unitPrice = 100.0, initialPayment = 0.0, total = 100.0, balance = 0.0),
            Order(id = 2, customerName = "Maria Lopez", deliveryDate = 0, description = "Pantalon", quantity = 2, unitPrice = 50.0, initialPayment = 0.0, total = 100.0, balance = 0.0)
        )
        fakeRepository.emit(orders)
        
        vm.state.test {
            // The initial state or the state after init collection
            var state = awaitItem()
            // Skip initial Loading if it was already emitted
            if (state is OrderState.Loading) {
                state = awaitItem()
            }
            assertTrue(state is OrderState.Success)
            assertEquals(2, (state as OrderState.Success).orders.size)
        }
    }

    @Test
    fun `given empty orders list, state is Success with empty list`() = runTest {
        fakeRepository.emit(emptyList())
        
        vm.state.test {
            var state = awaitItem()
            if (state is OrderState.Loading) {
                state = awaitItem()
            }
            assertTrue(state is OrderState.Success)
            assertTrue((state as OrderState.Success).orders.isEmpty())
        }
    }

    @Test
    fun `given OnRefresh intent, reloads orders`() = runTest {
        val orders = listOf(
            Order(id = 1, customerName = "Test", deliveryDate = 0, description = "Test", quantity = 1, unitPrice = 10.0, initialPayment = 0.0, total = 10.0, balance = 0.0)
        )
        fakeRepository.emit(orders)
        
        vm.onIntent(OrderIntent.OnRefresh)

        vm.state.test {
            var state = awaitItem()
            if (state is OrderState.Loading) {
                state = awaitItem()
            }
            assertTrue(state is OrderState.Success)
            assertEquals(1, (state as OrderState.Success).orders.size)
        }
    }
}

class FakeOrderRepository : OrderRepository {
    private val ordersFlow = MutableStateFlow<List<Order>>(emptyList())

    fun emit(orders: List<Order>) {
        ordersFlow.value = orders
    }

    override fun getAllOrders(): Flow<List<Order>> = ordersFlow

    override suspend fun saveOrder(order: Order): Long = 0
    override suspend fun getUniqueCustomerNames(query: String): List<String> = emptyList()
    override suspend fun getPendingOrders(): List<Order> = emptyList()
    override suspend fun markAsSynced(orderId: Long) {}
}
