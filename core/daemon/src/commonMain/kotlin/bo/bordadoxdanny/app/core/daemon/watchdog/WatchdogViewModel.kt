package bo.bordadoxdanny.app.core.daemon.watchdog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bo.bordadoxdanny.app.core.daemon.db.SyncQueueDao
import bo.bordadoxdanny.app.core.daemon.model.HeartbeatModel
import bo.bordadoxdanny.app.core.daemon.notifications.NotificationHelper
import bo.bordadoxdanny.app.core.daemon.repository.HeartbeatRepository
import bo.bordadoxdanny.app.core.daemon.worker.HeartbeatScheduler
import kotlinx.datetime.Clock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.Volatile

class WatchdogViewModel(
    private val deviceId: String,
    private val repository: HeartbeatRepository,
    private val syncQueueDao: SyncQueueDao,
    private val context: Any?
) : ViewModel() {

    private val _watchdogState = MutableStateFlow<WatchdogState>(WatchdogState.Idle)
    val watchdogState: StateFlow<WatchdogState> = _watchdogState.asStateFlow()

    @Volatile
    private var lastKnownHeartbeat: HeartbeatModel? = null

    init {
        // Parallel coroutine 1: Keep last known heartbeat updated from Firebase
        viewModelScope.launch {
            repository.observeHeartbeat(deviceId).collect { heartbeat ->
                lastKnownHeartbeat = heartbeat
                withContext(Dispatchers.Main) {
                    if (heartbeat != null && (_watchdogState.value is WatchdogState.Idle || _watchdogState.value is WatchdogState.Failure)) {
                        _watchdogState.value = WatchdogState.Healthy(heartbeat.timestampMillis)
                    }
                }
            }
        }

        // Parallel coroutine 2: Independent ticker to check for failures on a fixed interval
        viewModelScope.launch {
            while (true) {
                delay(5_000L)
                WatchdogService.check(lastKnownHeartbeat) {
                    viewModelScope.launch(Dispatchers.Main) {
                        if (_watchdogState.value !is WatchdogState.Failure && _watchdogState.value !is WatchdogState.Recovering) {
                            val missedSince = lastKnownHeartbeat?.timestampMillis ?: Clock.System.now().toEpochMilliseconds()
                            _watchdogState.value = WatchdogState.Failure(missedSince)
                            NotificationHelper.showFailureNotification(context, missedSince)
                            autoRepair()
                        }
                    }
                }
            }
        }
    }

    suspend fun autoRepair() {
        println("REPAIR: autoRepair called")
        withContext(Dispatchers.Main) {
            _watchdogState.value = WatchdogState.Recovering
        }
        try {
            withContext(Dispatchers.Default) {
                syncQueueDao.resetFailedToRetry()
            }
            HeartbeatScheduler.start(context)
            NotificationHelper.showRecoveryNotification(context)
            withContext(Dispatchers.Main) {
                _watchdogState.value = WatchdogState.Healthy(Clock.System.now().toEpochMilliseconds())
                println("REPAIR: autoRepair success")
            }
        } catch (e: Exception) {
            println("REPAIR: autoRepair failed: ${e.message}")
            withContext(Dispatchers.Main) {
                _watchdogState.value = WatchdogState.Failure(Clock.System.now().toEpochMilliseconds())
            }
        }
    }
}
