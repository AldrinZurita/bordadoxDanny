package bo.bordadoxdanny.app.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import bo.bordadoxdanny.app.features.config.domain.ConfigRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncConfigWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters), KoinComponent {

    private val configRepository: ConfigRepository by inject()

    override suspend fun doWork(): Result {
        Log.d("BORDADOS_SYNC", "SyncConfigWorker: Iniciando sincronización...")
        val result = configRepository.syncConfig()
        
        return if (result.isSuccess) {
            Log.d("BORDADOS_SYNC", "SyncConfigWorker: Éxito al sincronizar")
            Result.success()
        } else {
            val error = result.exceptionOrNull()?.message ?: "Error desconocido"
            Log.e("BORDADOS_SYNC", "SyncConfigWorker: Falló sincronización: $error")
            
            // Si el error es que no hay parámetros o falla de red, reintentamos
            Result.retry()
        }
    }
}
