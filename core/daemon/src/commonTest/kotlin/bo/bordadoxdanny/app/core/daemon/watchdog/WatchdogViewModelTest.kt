package bo.bordadoxdanny.app.core.daemon.watchdog

import app.cash.turbine.test
import bo.bordadoxdanny.app.core.daemon.db.FakeSyncQueueDao
import bo.bordadoxdanny.app.core.daemon.model.HeartbeatModel
import bo.bordadoxdanny.app.core.daemon.repository.FakeHeartbeatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class WatchdogViewModelTest {
    private lateinit var viewModel: WatchdogViewModel
    private lateinit var repository: FakeHeartbeatRepository
    private lateinit var dao: FakeSyncQueueDao
    private val testDispatcher = UnconfinedTestDispatcher()

    private val HEARTBEAT_THRESHOLD_MILLIS = 5 * 60 * 1000L

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeHeartbeatRepository()
        dao = FakeSyncQueueDao()
        viewModel = WatchdogViewModel(
            deviceId = "test-device",
            repository = repository,
            syncQueueDao = dao,
            context = null
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Idle`() = runTest {
        viewModel.watchdogState.test {
            assertEquals(WatchdogState.Idle, awaitItem())
        }
    }

    @Test
    fun `fresh heartbeat transitions to Healthy`() = runTest {
        viewModel.watchdogState.test {
            assertEquals(WatchdogState.Idle, awaitItem())
            
            val freshHeartbeat = HeartbeatModel("test-device", System.currentTimeMillis(), "ALIVE")
            repository.emitHeartbeat(freshHeartbeat)
            
            val state = awaitItem()
            assertTrue(state is WatchdogState.Healthy)
        }
    }

    @Test
    fun `stale heartbeat transitions to Failure`() = runTest {
        viewModel.watchdogState.test {
            assertEquals(WatchdogState.Idle, awaitItem())
            
            val staleTime = System.currentTimeMillis() - HEARTBEAT_THRESHOLD_MILLIS - 1000L
            val staleHeartbeat = HeartbeatModel("test-device", staleTime, "ALIVE")
            repository.emitHeartbeat(staleHeartbeat)
            
            // WatchdogViewModel calls autoRepair() which transitions state
            assertTrue(awaitItem() is WatchdogState.Failure)
            assertEquals(WatchdogState.Recovering, awaitItem())
            assertTrue(awaitItem() is WatchdogState.Healthy)
        }
    }

    @Test
    fun `null emission transitions to Failure`() = runTest {
        viewModel.watchdogState.test {
            assertEquals(WatchdogState.Idle, awaitItem())
            
            repository.emitHeartbeat(null)
            
            assertTrue(awaitItem() is WatchdogState.Failure)
            assertEquals(WatchdogState.Recovering, awaitItem())
            assertTrue(awaitItem() is WatchdogState.Healthy)
        }
    }

    @Test
    fun `autoRepair transitions through Recovering then back to Healthy`() = runTest {
        // Force Failure state first by emitting null and letting it finish its auto-recovery
        repository.emitHeartbeat(null)
        
        viewModel.watchdogState.test {
            // Should be Healthy now from the previous emission's auto-repair
            assertTrue(awaitItem() is WatchdogState.Healthy)
            
            viewModel.autoRepair()
            
            assertEquals(WatchdogState.Recovering, awaitItem())
            assertTrue(awaitItem() is WatchdogState.Healthy)
        }
    }

    @Test
    fun `autoRepair throws exception returns to Failure`() = runTest {
        dao.shouldThrow = true
        
        viewModel.watchdogState.test {
            // Initial state from setup
            assertEquals(WatchdogState.Idle, awaitItem())
            
            viewModel.autoRepair()
            
            assertEquals(WatchdogState.Recovering, awaitItem())
            val finalState = awaitItem()
            assertTrue(finalState is WatchdogState.Failure)
        }
    }
}
