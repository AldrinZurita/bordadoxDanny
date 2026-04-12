package bo.bordadoxdanny.app.domain.cash

import kotlinx.coroutines.flow.Flow

interface CashRepository {
    fun getAllCashEntries(): Flow<List<CashEntry>>
    suspend fun saveCashEntry(entry: CashEntry)
}
