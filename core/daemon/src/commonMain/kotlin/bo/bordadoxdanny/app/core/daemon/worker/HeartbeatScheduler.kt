package bo.bordadoxdanny.app.core.daemon.worker

expect object HeartbeatScheduler {
    fun start(context: Any?)
    fun stop(context: Any?)
}
