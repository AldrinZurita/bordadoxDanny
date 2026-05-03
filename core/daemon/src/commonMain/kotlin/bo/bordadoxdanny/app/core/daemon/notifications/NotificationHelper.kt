package bo.bordadoxdanny.app.core.daemon.notifications

expect object NotificationHelper {
    fun createChannel(context: Any?)
    fun showRecoveryNotification(context: Any?)
    fun showFailureNotification(context: Any?, missedSinceMillis: Long)
}
