package bo.bordadoxdanny.app.features.cash.domain

import kotlinx.coroutines.flow.Flow

class GetCashEntriesUseCase(private val repository: CashRepository) {
    operator fun invoke(): Flow<List<CashEntry>> = repository.getAllCashEntries()
}
