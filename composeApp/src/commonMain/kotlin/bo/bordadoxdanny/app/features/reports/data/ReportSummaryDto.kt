package bo.bordadoxdanny.app.features.reports.data

import kotlinx.serialization.Serializable

@Serializable
data class ReportSummaryDto(
    val periodId: String = "",
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val netProfit: Double = 0.0,
    val accountsReceivable: Double = 0.0,
    val year: Int? = null,
    val month: Int? = null,
    val updatedAt: Long = 0L
)
