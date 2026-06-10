package bo.bordadoxdanny.app.features.cash.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import bo.bordadoxdanny.app.core.sync.SyncConfig
import bo.bordadoxdanny.app.features.cash.data.TransactionDao
import bo.bordadoxdanny.app.features.cash.data.toDto
import bo.bordadoxdanny.app.firebase.FirebaseManager

class SyncTransactionWorker(
    context: Context,
    params: WorkerParameters,
    private val transactionDao: TransactionDao,
    private val firebaseManager: FirebaseManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        // CA-04: UID Inyectado como Data
        val userId = inputData.getString(SyncConfig.KEY_USER_ID) ?: return Result.failure()

        // PROTOCOLO ANTI-LOOP: Solo procesamos registros PENDING
        val pendingTransactions = transactionDao.getPendingTransactions(userId)
        if (pendingTransactions.isEmpty()) return Result.success()

        return try {
            pendingTransactions.forEach { entity ->
                val dto = entity.toDto()
                // Nodo unificado: transactions/{userId}/{id}
                firebaseManager.saveData("transactions/$userId/${entity.id}", dto).getOrThrow()
                transactionDao.markAsSynced(entity.id, userId)
            }
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}
