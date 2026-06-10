package bo.bordadoxdanny.app.features.orders.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import bo.bordadoxdanny.app.core.sync.SyncConfig
import bo.bordadoxdanny.app.features.orders.data.toDto
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
        val userId = inputData.getString(SyncConfig.KEY_USER_ID) ?: return Result.failure()

        return try {
            val pendingOrders = repository.getPendingOrders(userId)
            if (pendingOrders.isEmpty()) return Result.success()

            pendingOrders.forEach { order ->
                // Nodo unificado: orders/{userId}/{orderId}
                val result = firebaseManager.saveData("orders/$userId/${order.id}", order.toDto())
                if (result.isSuccess) {
                    repository.markAsSynced(order.id, userId)
                }
            }
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}
