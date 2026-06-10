package bo.bordadoxdanny.app.features.reports.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportSummaryDao {
    @Query("SELECT * FROM report_summaries WHERE periodId = :periodId AND userId = :userId")
    fun getSummaryById(periodId: String, userId: String): Flow<ReportSummaryEntity?>

    @Query("SELECT * FROM report_summaries WHERE userId = :userId")
    fun getAllSummaries(userId: String): Flow<List<ReportSummaryEntity>>

    @Query("SELECT * FROM report_summaries WHERE userId = :userId AND syncStatus = 'PENDING'")
    suspend fun getPendingSummaries(userId: String): List<ReportSummaryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(summary: ReportSummaryEntity)

    @Query("UPDATE report_summaries SET syncStatus = :status WHERE periodId = :periodId AND userId = :userId")
    suspend fun updateSyncStatus(periodId: String, userId: String, status: String)

    @Query("DELETE FROM report_summaries WHERE userId = :userId")
    suspend fun deleteAll(userId: String)

    @Transaction
    suspend fun updateSummaries(userId: String, summaries: List<ReportSummaryEntity>) {
        deleteAll(userId)
        summaries.forEach { upsert(it) }
    }
}
