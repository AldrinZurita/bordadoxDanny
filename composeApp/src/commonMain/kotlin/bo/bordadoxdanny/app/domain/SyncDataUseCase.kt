package bo.bordadoxdanny.app.domain

import kotlin.Result

class SyncDataUseCase {
    suspend fun execute(): Result<Unit> {
        return try {
            // Simulación de sincronización
            println("Sincronizando datos...")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
