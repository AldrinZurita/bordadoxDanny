package bo.bordadoxdanny.app.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent

class LogUploadWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters), KoinComponent {

    override suspend fun doWork(): Result {
        return try {
            // Aquí irá la lógica de tu caso de uso más adelante
            println("WorkManager: Ejecutando PRINT de prueba donde se deberia poner el caso de USO BRO...")
            
            // Si todo sale bien
            Result.success()
        } catch (e: Exception) {
            println("WorkManager: Error en la tarea: ${e.message}")
            // Si falla, WorkManager puede reintentar según la política
            Result.retry()
        }
    }
}
