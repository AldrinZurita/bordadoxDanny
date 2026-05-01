package bo.bordadoxdanny.app.core.daemon.watchdog

import bo.bordadoxdanny.app.core.daemon.model.HeartbeatModel

actual object WatchdogService {
    private const val HEARTBEAT_THRESHOLD_MILLIS = 5 * 60 * 1000L
    //val HEARTBEAT_THRESHOLD_MILLIS = 10_000L

    actual fun check(lastHeartbeat: HeartbeatModel?, onFailure: () -> Unit) {
        val now = System.currentTimeMillis()
        if (lastHeartbeat == null || now - lastHeartbeat.timestampMillis > HEARTBEAT_THRESHOLD_MILLIS) {
            onFailure()
        }
    }
}
