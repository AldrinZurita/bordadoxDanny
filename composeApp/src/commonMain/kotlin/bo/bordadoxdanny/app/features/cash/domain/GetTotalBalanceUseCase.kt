package bo.bordadoxdanny.app.features.cash.domain

import kotlinx.coroutines.flow.Flow

class GetTotalBalanceUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(type: TransactionType): Flow<Double> {
        return repository.getTotalByType(type)
    }
}
