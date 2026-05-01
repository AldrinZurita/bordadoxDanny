package bo.bordadoxdanny.app.core.daemon.watchdog

sealed class WatchdogState {
    object Idle : WatchdogState()
    data class Healthy(val lastSeen: Long) : WatchdogState()
    data class Failure(val missedSince: Long) : WatchdogState()
    object Recovering : WatchdogState()
}
