package bo.bordadoxdanny.app.features.reports.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportSummaryDao {
    @Query("SELECT * FROM report_summaries WHERE periodId = :periodId")
    fun getSummaryById(periodId: String): Flow<ReportSummaryEntity?>

    @Query("SELECT * FROM report_summaries")
    fun getAllSummaries(): Flow<List<ReportSummaryEntity>>

    @Query("SELECT * FROM report_summaries WHERE syncStatus = 'PENDING'")
    suspend fun getPendingSummaries(): List<ReportSummaryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(summary: ReportSummaryEntity)

    @Query("UPDATE report_summaries SET syncStatus = :status WHERE periodId = :periodId")
    suspend fun updateSyncStatus(periodId: String, status: String)

    @Query("DELETE FROM report_summaries")
    suspend fun deleteAll()

    @Transaction
    suspend fun updateSummaries(summaries: List<ReportSummaryEntity>) {
        deleteAll()
        summaries.forEach { upsert(it) }
    }
}
