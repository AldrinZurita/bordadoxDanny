package bo.bordadoxdanny.app.core.daemon.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SyncQueueDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: SyncQueueDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.syncQueueDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `insertAll then getByStatus returns all inserted rows`() = runBlocking {
        val items = listOf(
            SyncQueueEntity(payload = "1", status = "PENDING", createdAt = 1000L),
            SyncQueueEntity(payload = "2", status = "PENDING", createdAt = 1001L),
            SyncQueueEntity(payload = "3", status = "PENDING", createdAt = 1002L)
        )
        dao.insertAll(items)
        
        val result = dao.getByStatus("PENDING")
        assertEquals(3, result.size)
    }

    @Test
    fun `updateStatus changes a single row others unaffected`() = runBlocking {
        val items = listOf(
            SyncQueueEntity(id = 1, payload = "1", status = "PENDING", createdAt = 1000L),
            SyncQueueEntity(id = 2, payload = "2", status = "PENDING", createdAt = 1001L)
        )
        dao.insertAll(items)
        
        dao.updateStatus(1, "FAILED")
        
        val failed = dao.getByStatus("FAILED")
        val pending = dao.getByStatus("PENDING")
        
        assertEquals(1, failed.size)
        assertEquals(1, pending.size)
        assertEquals(1, failed[0].id)
        assertEquals(2, pending[0].id)
    }

    @Test
    fun `deleteCompleted removes only COMPLETED rows`() = runBlocking {
        val items = listOf(
            SyncQueueEntity(payload = "1", status = "PENDING", createdAt = 1000L),
            SyncQueueEntity(payload = "2", status = "COMPLETED", createdAt = 1001L),
            SyncQueueEntity(payload = "3", status = "FAILED", createdAt = 1002L)
        )
        dao.insertAll(items)
        
        dao.deleteCompleted()
        
        val pending = dao.getByStatus("PENDING")
        val failed = dao.getByStatus("FAILED")
        val completed = dao.getByStatus("COMPLETED")
        
        assertEquals(1, pending.size)
        assertEquals(1, failed.size)
        assertEquals(0, completed.size)
    }

    @Test
    fun `resetFailedToRetry sets all FAILED rows back to PENDING with retryCount 0`() = runBlocking {
        val items = listOf(
            SyncQueueEntity(payload = "1", status = "FAILED", retryCount = 3, createdAt = 1000L),
            SyncQueueEntity(payload = "2", status = "FAILED", retryCount = 5, createdAt = 1001L)
        )
        dao.insertAll(items)
        
        dao.resetFailedToRetry()
        
        val pending = dao.getByStatus("PENDING")
        assertEquals(2, pending.size)
        assertTrue(pending.all { it.retryCount == 0 })
    }

    @Test
    fun `resetFailedToRetry does not affect PENDING or IN_PROGRESS rows`() = runBlocking {
        val items = listOf(
            SyncQueueEntity(payload = "1", status = "PENDING", retryCount = 0, createdAt = 1000L),
            SyncQueueEntity(payload = "2", status = "IN_PROGRESS", retryCount = 1, createdAt = 1001L),
            SyncQueueEntity(payload = "3", status = "FAILED", retryCount = 2, createdAt = 1002L)
        )
        dao.insertAll(items)
        
        dao.resetFailedToRetry()
        
        val pending = dao.getByStatus("PENDING")
        val inProgress = dao.getByStatus("IN_PROGRESS")
        val failed = dao.getByStatus("FAILED")
        
        assertEquals(2, pending.size) // Original PENDING + Reset FAILED
        assertEquals(1, inProgress.size)
        assertEquals(0, failed.size)
        
        assertTrue(inProgress[0].retryCount == 1)
    }
}
