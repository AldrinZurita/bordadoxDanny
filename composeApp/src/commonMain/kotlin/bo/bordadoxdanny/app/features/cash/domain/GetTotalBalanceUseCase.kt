package bo.bordadoxdanny.app.features.cash.domain

import kotlinx.coroutines.flow.Flow

class GetTotalBalanceUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(userId: String, type: TransactionType): Flow<Double> {
        return repository.getTotalByType(userId, type)
    }
}
