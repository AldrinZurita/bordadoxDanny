package bo.bordadoxdanny.app.features.cash.data

import bo.bordadoxdanny.app.firebase.FirebaseManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TransactionFirebaseListener(
    private val firebaseManager: FirebaseManager,
    private val transactionDao: TransactionDao,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    fun startListening(userId: String) {
        // Observamos el nodo de transacciones del usuario
        // Nota: Si FirebaseManager.observeData observa un nodo que es una lista/mapa, 
        // el DTO debe ser capaz de manejarlo o se requiere una implementación de lista.
        // Basado en ReportFirebaseListener, se asume que se dispara por cada cambio relevante.
        firebaseManager.observeData("transactions/$userId", TransactionDto::class)
            .onEach { dto ->
                dto?.let {
                    transactionDao.insertTransaction(it.toEntity())
                }
            }
            .launchIn(scope)
    }
}
