package bo.bordadoxdanny.app.data.cash

import bo.bordadoxdanny.app.domain.cash.CashEntry
import bo.bordadoxdanny.app.domain.cash.CashRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CashRepositoryImpl(private val dao: CashDao) : CashRepository {
    override fun getAllCashEntries(): Flow<List<CashEntry>> = dao.getAllCashEntries().map { entities ->
        entities.map { CashEntry(it.id, it.amount, it.type, it.reason, it.timestamp) }
    }

    override suspend fun saveCashEntry(entry: CashEntry) {
        dao.insert(CashEntity(amount = entry.amount, type = entry.type, reason = entry.reason, timestamp = entry.timestamp))
    }
}
