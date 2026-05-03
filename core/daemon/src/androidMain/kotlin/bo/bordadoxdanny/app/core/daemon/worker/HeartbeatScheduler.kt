package bo.bordadoxdanny.app.core.daemon.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

actual object HeartbeatScheduler {
    private const val WORK_TAG = "heartbeat_worker"

    actual fun start(context: Any?) {
        val ctx = context as Context
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<HeartbeatWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
            .addTag(WORK_TAG)
            .build()

        WorkManager.getInstance(ctx).enqueueUniquePeriodicWork(
            WORK_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    actual fun stop(context: Any?) {
        val ctx = context as Context
        WorkManager.getInstance(ctx).cancelAllWorkByTag(WORK_TAG)
    }
}
