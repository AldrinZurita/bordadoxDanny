package bo.bordadoxdanny.app.core.daemon.repository

import bo.bordadoxdanny.app.core.daemon.model.HeartbeatModel
import kotlinx.coroutines.flow.Flow

interface HeartbeatRepository {
    suspend fun writeHeartbeat(heartbeat: HeartbeatModel)
    fun observeHeartbeat(deviceId: String): Flow<HeartbeatModel?>
}
