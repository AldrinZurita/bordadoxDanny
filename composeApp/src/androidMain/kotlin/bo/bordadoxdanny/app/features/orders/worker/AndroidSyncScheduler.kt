package bo.bordadoxdanny.app.features.orders.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import bo.bordadoxdanny.app.features.orders.data.SyncScheduler
import bo.bordadoxdanny.app.features.cash.worker.SyncTransactionWorker

class AndroidSyncScheduler(private val context: Context) : SyncScheduler {
    override fun scheduleOrderSync() {
        val syncRequest = OneTimeWorkRequestBuilder<OrderSyncWorker>()
            .setConstraints(createConstraints())
            .build()
        WorkManager.getInstance(context).enqueue(syncRequest)
    }

    override fun scheduleTransactionSync() {
        val syncRequest = OneTimeWorkRequestBuilder<SyncTransactionWorker>()
            .setConstraints(createConstraints())
            .build()
        WorkManager.getInstance(context).enqueue(syncRequest)
    }

    private fun createConstraints() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
}
