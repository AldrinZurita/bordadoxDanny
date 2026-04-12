package bo.bordadoxdanny.app.domain.cash

import kotlinx.coroutines.flow.Flow

class GetCashEntriesUseCase(private val repository: CashRepository) {
    operator fun invoke(): Flow<List<CashEntry>> = repository.getAllCashEntries()
}
