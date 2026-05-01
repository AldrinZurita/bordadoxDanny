package bo.bordadoxdanny.app.core.daemon.worker

import android.content.Context
import android.provider.Settings
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import bo.bordadoxdanny.app.core.daemon.model.HeartbeatModel
import bo.bordadoxdanny.app.core.daemon.repository.FirebaseHeartbeatRepository

class HeartbeatWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val deviceId = Settings.Secure.getString(
            applicationContext.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "unknown_device"

        val repository = FirebaseHeartbeatRepository()

        return try {
            val heartbeat = HeartbeatModel(
                deviceId = deviceId,
                timestampMillis = System.currentTimeMillis(),
                status = "ALIVE"
            )
            repository.writeHeartbeat(heartbeat)
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) {
                try {
                    repository.writeHeartbeat(
                        HeartbeatModel(deviceId, System.currentTimeMillis(), "DEGRADED")
                    )
                } catch (ignored: Exception) {}
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }
}
