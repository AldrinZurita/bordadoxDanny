package bo.bordadoxdanny.app.features.cash.domain

import kotlinx.coroutines.flow.Flow

class GetTransactionsUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(userId: String, type: TransactionType): Flow<List<Transaction>> {
        return repository.getTransactionsByType(userId, type)
    }
}
