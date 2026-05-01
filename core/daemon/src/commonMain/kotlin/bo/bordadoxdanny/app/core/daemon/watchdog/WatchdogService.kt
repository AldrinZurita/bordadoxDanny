package bo.bordadoxdanny.app.core.daemon.watchdog

import bo.bordadoxdanny.app.core.daemon.model.HeartbeatModel

expect object WatchdogService {
    fun check(lastHeartbeat: HeartbeatModel?, onFailure: () -> Unit)
}
