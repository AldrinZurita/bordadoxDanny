package bo.bordadoxdanny.app.features.cash.data

import bo.bordadoxdanny.app.features.cash.domain.Transaction
import bo.bordadoxdanny.app.features.cash.domain.TransactionRepository
import bo.bordadoxdanny.app.features.cash.domain.TransactionType
import bo.bordadoxdanny.app.features.orders.data.SyncScheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao,
    private val syncScheduler: SyncScheduler
) : TransactionRepository {

    override fun getTransactionsByType(userId: String, type: TransactionType): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByType(userId, type.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTotalByType(userId: String, type: TransactionType): Flow<Double> {
        return transactionDao.getTotalByType(userId, type.name).map { it ?: 0.0 }
    }

    override suspend fun addTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction.toEntity())
        syncScheduler.scheduleTransactionSync(transaction.userId)
    }

    override suspend fun getPendingTransactions(userId: String): List<Transaction> {
        return transactionDao.getPendingTransactions(userId).map { it.toDomain() }
    }

    override suspend fun markAsSynced(id: Long, userId: String) {
        transactionDao.markAsSynced(id, userId)
    }

    override fun getAllTransactionTimestamps(userId: String): Flow<List<Long>> {
        return transactionDao.getAllTransactionTimestamps(userId)
    }
}
