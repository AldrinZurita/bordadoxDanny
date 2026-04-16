package bo.bordadoxdanny.app.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class LogScheduler(
    private val context: Context
) {
    companion object {
        private const val LOG_WORKNAME = "logUploadWork"
        private const val INTERVAL_MINUTES = 15L
    }

    // Tarea Periódica (Cada 15 min)
    fun schedulePeriodicUpload() {
        val logRequest = PeriodicWorkRequest.Builder(
            LogUploadWorker::class.java,
            INTERVAL_MINUTES,
            TimeUnit.MINUTES
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            ).build()

        WorkManager.getInstance(context.applicationContext).enqueueUniquePeriodicWork(
            LOG_WORKNAME,
            ExistingPeriodicWorkPolicy.KEEP,
            logRequest
        )
    }

    // Tarea Única de PRUEBA (Se ejecuta de inmediato)
    fun runImmediateTest() {
        val testRequest = OneTimeWorkRequestBuilder<LogUploadWorker>()
            .build()
        
        WorkManager.getInstance(context.applicationContext).enqueue(testRequest)
    }
}
