package bo.bordadoxdanny.app.data.cash

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CashDao {
    @Insert
    suspend fun insert(cash: CashEntity)

    @Query("SELECT * FROM cash_entries")
    fun getAllCashEntries(): Flow<List<CashEntity>>
}
