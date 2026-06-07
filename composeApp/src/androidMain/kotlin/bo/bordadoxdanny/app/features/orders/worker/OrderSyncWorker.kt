package bo.bordadoxdanny.app.features.orders.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import bo.bordadoxdanny.app.features.orders.domain.OrderRepository
import bo.bordadoxdanny.app.firebase.FirebaseManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OrderSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val repository: OrderRepository by inject()
    private val firebaseManager: FirebaseManager by inject()

    override suspend fun doWork(): Result {
        return try {
            val pendingOrders = repository.getPendingOrders()
            if (pendingOrders.isEmpty()) return Result.success()

            pendingOrders.forEach { order ->
                val result = firebaseManager.saveData("orders/${order.id}", order.toDto())
                if (result.isSuccess) {
                    repository.markAsSynced(order.id)
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
