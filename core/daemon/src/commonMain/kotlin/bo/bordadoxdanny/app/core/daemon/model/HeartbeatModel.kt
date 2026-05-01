package bo.bordadoxdanny.app.core.daemon.model

data class HeartbeatModel(
    val deviceId: String,
    val timestampMillis: Long,
    val status: String
)
