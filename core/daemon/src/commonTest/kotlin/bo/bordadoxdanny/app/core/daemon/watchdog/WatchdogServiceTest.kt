package bo.bordadoxdanny.app.core.daemon.watchdog

import bo.bordadoxdanny.app.core.daemon.model.HeartbeatModel
import kotlin.test.Test
import kotlin.test.assertEquals

class WatchdogServiceTest {

    private val HEARTBEAT_THRESHOLD_MILLIS = 5 * 60 * 1000L

    @Test
    fun `heartbeat is null calls onFailure`() {
        var failureCalled = 0
        WatchdogService.check(null) {
            failureCalled++
        }
        assertEquals(1, failureCalled)
    }

    @Test
    fun `heartbeat is fresh does not call onFailure`() {
        var failureCalled = 0
        val freshHeartbeat = HeartbeatModel(
            deviceId = "test",
            timestampMillis = System.currentTimeMillis() - 1000L,
            status = "ALIVE"
        )
        WatchdogService.check(freshHeartbeat) {
            failureCalled++
        }
        assertEquals(0, failureCalled)
    }

    @Test
    fun `heartbeat is exactly at threshold does not call onFailure`() {
        var failureCalled = 0
        val exactlyAtThreshold = HeartbeatModel(
            deviceId = "test",
            timestampMillis = System.currentTimeMillis() - HEARTBEAT_THRESHOLD_MILLIS,
            status = "ALIVE"
        )
        WatchdogService.check(exactlyAtThreshold) {
            failureCalled++
        }
        assertEquals(0, failureCalled)
    }

    @Test
    fun `heartbeat is 1ms past threshold calls onFailure`() {
        var failureCalled = 0
        val staleHeartbeat = HeartbeatModel(
            deviceId = "test",
            timestampMillis = System.currentTimeMillis() - HEARTBEAT_THRESHOLD_MILLIS - 1,
            status = "ALIVE"
        )
        WatchdogService.check(staleHeartbeat) {
            failureCalled++
        }
        assertEquals(1, failureCalled)
    }

    @Test
    fun `onFailure is never called more than once per check`() {
        var totalFailures = 0
        val staleHeartbeat = HeartbeatModel(
            deviceId = "test",
            timestampMillis = System.currentTimeMillis() - HEARTBEAT_THRESHOLD_MILLIS - 1000,
            status = "ALIVE"
        )
        
        WatchdogService.check(staleHeartbeat) { totalFailures++ }
        WatchdogService.check(staleHeartbeat) { totalFailures++ }
        
        assertEquals(2, totalFailures)
    }
}
