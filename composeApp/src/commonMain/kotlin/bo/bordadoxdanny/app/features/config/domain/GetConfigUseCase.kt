package bo.bordadoxdanny.app.features.config.domain

import kotlinx.coroutines.flow.Flow

class GetConfigUseCase(private val repository: ConfigRepository) {
    operator fun invoke(): Flow<Map<String, String>> = repository.getAllConfigs()
}
