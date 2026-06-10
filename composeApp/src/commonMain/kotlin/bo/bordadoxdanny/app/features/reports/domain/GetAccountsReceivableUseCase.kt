package bo.bordadoxdanny.app.features.reports.domain

import kotlinx.coroutines.flow.Flow

class GetAccountsReceivableUseCase(private val repository: ReportRepository) {
    operator fun invoke(userId: String): Flow<List<AccountsReceivableItem>> {
        return repository.getAccountsReceivable(userId)
    }
}
