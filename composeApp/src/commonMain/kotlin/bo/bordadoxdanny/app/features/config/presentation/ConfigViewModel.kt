package bo.bordadoxdanny.app.features.config.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.features.config.domain.GetConfigUseCase
import bo.bordadoxdanny.app.workers.WorkerScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class ConfigViewModel(
    getConfigUseCase: GetConfigUseCase,
    private val workerScheduler: WorkerScheduler
) : ViewModel() {

    val configs: StateFlow<Map<String, String>> = getConfigUseCase()
        .onEach { 
            // Si la base de datos está vacía, forzamos una sincronización
            if (it.isEmpty()) {
                workerScheduler.syncConfigNow()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )
}
