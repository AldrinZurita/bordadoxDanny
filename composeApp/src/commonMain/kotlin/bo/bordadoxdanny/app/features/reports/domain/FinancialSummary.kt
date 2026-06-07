package bo.bordadoxdanny.app.features.reports.domain

data class FinancialSummary(
    val totalIncome: Double,
    val totalExpenses: Double,
    val netProfit: Double,
    val accountsReceivable: Double,
    val period: Period
)
