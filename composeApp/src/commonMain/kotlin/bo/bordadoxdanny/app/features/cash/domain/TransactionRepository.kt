package bo.bordadoxdanny.app.features.cash.domain

import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>>
    fun getTotalByType(type: TransactionType): Flow<Double>
    suspend fun addTransaction(transaction: Transaction)
    suspend fun getPendingTransactions(): List<Transaction>
    suspend fun markAsSynced(id: Long)
}
