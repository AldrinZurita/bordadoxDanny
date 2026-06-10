package bo.bordadoxdanny.app.features.orders.worker

import android.content.Context
import androidx.work.*
import bo.bordadoxdanny.app.core.sync.SyncConfig
import bo.bordadoxdanny.app.features.orders.data.SyncScheduler
import bo.bordadoxdanny.app.features.cash.worker.SyncTransactionWorker

class AndroidSyncScheduler(private val context: Context) : SyncScheduler {
    override fun scheduleOrderSync(userId: String) {
        val inputData = workDataOf(SyncConfig.KEY_USER_ID to userId)
        val syncRequest = OneTimeWorkRequestBuilder<OrderSyncWorker>()
            .setInputData(inputData)
            .setConstraints(createConstraints())
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            "OrderSync_$userId",
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            syncRequest
        )
    }

    override fun scheduleTransactionSync(userId: String) {
        val inputData = workDataOf(SyncConfig.KEY_USER_ID to userId)
        val syncRequest = OneTimeWorkRequestBuilder<SyncTransactionWorker>()
            .setInputData(inputData)
            .setConstraints(createConstraints())
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            "TransactionSync_$userId",
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            syncRequest
        )
    }

    private fun createConstraints() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
}
