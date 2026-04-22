package bo.bordadoxdanny.app.workers

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class LogScheduler(private val context: Context) : WorkerScheduler {
    
    // Método para producción (Cada 15 min)
    override fun scheduleLogUpload() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val logRequest = PeriodicWorkRequestBuilder<LogUploadWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .addTag("LOG_WORK_TAG")
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "PeriodicLogUpload",
            ExistingPeriodicWorkPolicy.KEEP,
            logRequest
        )
    }

    // 🔥 MÉTODO DE PRUEBA: Ejecuta el worker inmediatamente
    override fun testWorkImmediately() {
        val testRequest = OneTimeWorkRequestBuilder<LogUploadWorker>()
            .addTag("LOG_WORK_TAG")
            .build()

        WorkManager.getInstance(context).enqueue(testRequest)
    }
}
