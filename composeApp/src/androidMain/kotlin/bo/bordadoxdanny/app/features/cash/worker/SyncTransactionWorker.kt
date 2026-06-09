package bo.bordadoxdanny.app.features.cash.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import bo.bordadoxdanny.app.features.cash.data.TransactionDao
import bo.bordadoxdanny.app.features.cash.data.toDto
import bo.bordadoxdanny.app.firebase.FirebaseManager
import com.google.firebase.auth.FirebaseAuth

class SyncTransactionWorker(
    context: Context,
    params: WorkerParameters,
    private val transactionDao: TransactionDao,
    private val firebaseManager: FirebaseManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?: return Result.failure()

        val pendingTransactions = transactionDao.getPendingTransactions()
        if (pendingTransactions.isEmpty()) return Result.success()

        return try {
            pendingTransactions.forEach { entity ->
                val dto = entity.toDto()
                firebaseManager.saveData("transactions/$userId/${entity.id}", dto).getOrThrow()
                transactionDao.markAsSynced(entity.id)
            }
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}
