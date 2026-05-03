package bo.bordadoxdanny.app.core.daemon.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import bo.bordadoxdanny.app.core.daemon.repository.FirebaseHeartbeatRepository
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class HeartbeatWorkerTest {
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        WorkManagerTestInitHelper.initializeTestWorkManager(context)
        mockkConstructor(FirebaseHeartbeatRepository::class)
    }

    @After
    fun tearDown() {
        unmockkConstructor(FirebaseHeartbeatRepository::class)
    }

    @Test
    fun `worker returns SUCCESS when writeHeartbeat succeeds`() = runBlocking {
        coEvery { anyConstructed<FirebaseHeartbeatRepository>().writeHeartbeat(any()) } just Runs

        val worker = TestListenableWorkerBuilder<HeartbeatWorker>(context).build()
        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.success(), result)
    }

    @Test
    fun `worker returns RETRY when writeHeartbeat throws a network exception`() = runBlocking {
        coEvery { anyConstructed<FirebaseHeartbeatRepository>().writeHeartbeat(any()) } throws IOException("network error")

        val worker = TestListenableWorkerBuilder<HeartbeatWorker>(context).build()
        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.retry(), result)
    }

    @Test
    fun `worker caps retries at 3 and returns FAILURE on the 4th attempt`() = runBlocking {
        coEvery { anyConstructed<FirebaseHeartbeatRepository>().writeHeartbeat(any()) } throws IOException("network error")

        val worker = TestListenableWorkerBuilder<HeartbeatWorker>(context)
            .setRunAttemptCount(3)
            .build()
        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.failure(), result)
    }

    @Test
    fun `HeartbeatScheduler start enqueues exactly one PeriodicWorkRequest tagged heartbeat_worker`() {
        HeartbeatScheduler.start(context)

        val workInfos = WorkManager.getInstance(context).getWorkInfosByTag("heartbeat_worker").get()
        assertEquals(1, workInfos.size)
    }

    @Test
    fun `HeartbeatScheduler stop cancels the enqueued work`() {
        HeartbeatScheduler.start(context)
        HeartbeatScheduler.stop(context)

        val workInfos = WorkManager.getInstance(context).getWorkInfosByTag("heartbeat_worker").get()
        // WorkManager.cancelAllWorkByTag doesn't remove the info, it changes state to CANCELLED
        assertEquals("CANCELLED", workInfos[0].state.name)
    }
}
