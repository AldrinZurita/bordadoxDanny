package bo.bordadoxdanny.app.features.cash.domain

import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactionsByType(userId: String, type: TransactionType): Flow<List<Transaction>>
    fun getTotalByType(userId: String, type: TransactionType): Flow<Double>
    suspend fun addTransaction(transaction: Transaction)
    suspend fun getPendingTransactions(userId: String): List<Transaction>
    suspend fun markAsSynced(id: Long, userId: String)
    fun getAllTransactionTimestamps(userId: String): Flow<List<Long>>
}
