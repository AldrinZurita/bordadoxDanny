package bo.bordadoxdanny.app.core.daemon.repository

import bo.bordadoxdanny.app.core.daemon.model.HeartbeatModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeHeartbeatRepository : HeartbeatRepository {
    private val heartbeatFlow = MutableSharedFlow<HeartbeatModel?>(replay = 1)

    override suspend fun writeHeartbeat(heartbeat: HeartbeatModel) {
        heartbeatFlow.emit(heartbeat)
    }

    override fun observeHeartbeat(deviceId: String): Flow<HeartbeatModel?> {
        return heartbeatFlow
    }

    suspend fun emitHeartbeat(h: HeartbeatModel?) {
        heartbeatFlow.emit(h)
    }
}
