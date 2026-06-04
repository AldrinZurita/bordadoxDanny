package bo.bordadoxdanny.app.features.orders.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import bo.bordadoxdanny.app.features.orders.data.SyncScheduler

class AndroidSyncScheduler(private val context: Context) : SyncScheduler {
    override fun scheduleOrderSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<OrderSyncWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueue(syncRequest)
    }
}
