package bo.bordadoxdanny.app.features.orders.data

import bo.bordadoxdanny.app.firebase.FirebaseManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class OrderFirebaseListener(
    private val orderDao: OrderDao,
    private val firebaseManager: FirebaseManager
) {
    private var listenerJob: Job? = null

    fun startListening(userId: String) {
        stopListening()
        listenerJob = CoroutineScope(Dispatchers.IO).launch {
            // Usamos observeData con un Map para representar la colección de órdenes
            firebaseManager.observeData("orders/$userId", Map::class).collect { data ->
                data?.forEach { (key, value) ->
                    // En Firebase las colecciones llegan como Map<String, Any>
                    // Intentamos convertir el valor a OrderDto si FirebaseManager no lo hizo
                    if (value is OrderDto) {
                        processOrder(value)
                    }
                }
            }
        }
    }

    private suspend fun processOrder(dto: OrderDto) {
        // REGLA CRÍTICA PROTOCOLO ANTI-LOOP:
        // Forzamos syncStatus = SYNCED para que el Worker local no intente subirlo de nuevo
        val entity = dto.toEntity().copy(syncStatus = "SYNCED")
        orderDao.insert(entity)
    }

    fun stopListening() {
        listenerJob?.cancel()
        listenerJob = null
    }
}
