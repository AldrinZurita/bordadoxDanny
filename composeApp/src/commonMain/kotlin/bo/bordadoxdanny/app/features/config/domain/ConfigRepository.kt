package bo.bordadoxdanny.app.features.config.domain

import kotlinx.coroutines.flow.Flow

interface ConfigRepository {
    fun getAllConfigs(): Flow<Map<String, String>>
    suspend fun syncConfig(): Result<Unit>
    suspend fun getConfig(key: String): String?
}
