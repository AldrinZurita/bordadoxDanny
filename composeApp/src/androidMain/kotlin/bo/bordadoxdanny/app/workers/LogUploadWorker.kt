package bo.bordadoxdanny.app.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import bo.bordadoxdanny.app.domain.SyncDataUseCase

class LogUploadWorker(
    context: Context,
    params: WorkerParameters,
    private val syncUseCase: SyncDataUseCase
) : CoroutineWorker(context, params) {

    companion object {
        private const val TAG = "WORKER_DEBUG"
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "🚀 WORKER START: La tarea en segundo plano ha comenzado!")
        
        return try {
            val result = syncUseCase.execute()
            
            result.fold(
                onSuccess = { 
                    Log.d(TAG, "✅ WORKER SUCCESS: Sincronización completada")
                    Result.success() 
                },
                onFailure = { error -> 
                    Log.e(TAG, "❌ WORKER ERROR: ${error.message}")
                    if (runAttemptCount < 3) {
                        Log.w(TAG, "🔄 WORKER RETRY: Intento $runAttemptCount")
                        Result.retry() 
                    } else {
                        Log.e(TAG, "⚠️ WORKER FAILURE: Fallo definitivo")
                        Result.failure() 
                    }
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "💥 WORKER CRASH: ${e.message}")
            Result.failure()
        }
    }
}
