package bo.bordadoxdanny.app.features.cash.domain

import kotlinx.coroutines.flow.Flow

interface CashRepository {
    fun getAllCashEntries(): Flow<List<CashEntry>>
    suspend fun saveCashEntry(entry: CashEntry)
}
