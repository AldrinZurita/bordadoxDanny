package bo.bordadoxdanny.app.features.config.data

import bo.bordadoxdanny.app.features.config.domain.ConfigRepository
import bo.bordadoxdanny.app.firebase.RemoteConfigManager
import bo.bordadoxdanny.app.getPlatform
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class ConfigRepositoryImpl(
    private val configDao: ConfigDao,
    private val remoteConfigManager: RemoteConfigManager
) : ConfigRepository {

    override fun getAllConfigs(): Flow<Map<String, String>> {
        return configDao.getAllConfigs().map { entities ->
            entities.associate { it.key to it.value }
        }
    }

    override suspend fun syncConfig(): Result<Unit> = runCatching {
        val success = remoteConfigManager.fetchAndActivate()
        if (success) {
            val remoteConfigs = remoteConfigManager.getAllConfigs()
            
            // 🔍 COMPARA CON LO QUE TENEMOS EN ROOM
            val localConfigs = configDao.getAllConfigs().firstOrNull()?.associate { it.key to it.value } ?: emptyMap()
            
            var changesDetected = 0
            var lastMessage = ""

            remoteConfigs.forEach { (key, value) ->
                if (localConfigs[key] != value) {
                    changesDetected++
                    lastMessage = "El parámetro '$key' ahora es '$value'"
                }
            }

            // Si hay cambios y no es la primera vez que descargamos (para no spamear al instalar)
            if (changesDetected > 0 && localConfigs.isNotEmpty()) {
                getPlatform().showNotification(
                    title = "Configuración Actualizada",
                    message = if (changesDetected == 1) lastMessage else "Se actualizaron $changesDetected parámetros."
                )
            }

            val entities = remoteConfigs.map { ConfigEntity(it.key, it.value) }
            configDao.insertConfigs(entities)
        } else {
            throw Exception("Failed to fetch remote config")
        }
    }

    override suspend fun getConfig(key: String): String? {
        return configDao.getConfigByKey(key)?.value
    }
}
