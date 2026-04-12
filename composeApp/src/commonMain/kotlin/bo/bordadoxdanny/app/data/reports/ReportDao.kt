package bo.bordadoxdanny.app.data.reports

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Insert
    suspend fun insert(report: ReportEntity)

    @Query("SELECT * FROM reports")
    fun getAllReports(): Flow<List<ReportEntity>>
}
