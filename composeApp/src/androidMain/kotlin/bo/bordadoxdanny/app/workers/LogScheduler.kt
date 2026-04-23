package bo.bordadoxdanny.app.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import java.util.concurrent.TimeUnit

class LogScheduler(private val context: Context) : WorkerScheduler {
    
    override fun scheduleLogUpload() {
        val logRequest = PeriodicWorkRequestBuilder<LogUploadWorker>(15, TimeUnit.MINUTES)
            .addTag("LOG_WORK_TAG")
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "PeriodicLogUpload",
            ExistingPeriodicWorkPolicy.KEEP,
            logRequest
        )
    }

    override fun testWorkImmediately() {
        val testRequest = OneTimeWorkRequestBuilder<LogUploadWorker>()
            .build()
        WorkManager.getInstance(context).enqueue(testRequest)
    }

    override fun syncConfigNow() {
        Log.d("BORDADOS_SYNC", "LogScheduler: Solicitando ejecución de SyncConfigWorker...")
        
        // Quitamos constraints temporalmente para asegurar que corra en el emulador
        val syncRequest = OneTimeWorkRequestBuilder<SyncConfigWorker>()
            .addTag("CONFIG_SYNC_TAG")
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "InitialConfigSync",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )
    }
}
