package bo.bordadoxdanny.app.features.reports.data

import androidx.room.Entity

@Entity(
    tableName = "report_summaries",
    primaryKeys = ["periodId", "userId"]
)
data class ReportSummaryEntity(
    val periodId: String, // e.g., "ALL", "2026", "2026-06"
    val userId: String,
    val totalIncome: Double,
    val totalExpenses: Double,
    val netProfit: Double,
    val accountsReceivable: Double,
    val year: Int?,
    val month: Int?,
    val syncStatus: String = "PENDING",
    val updatedAt: Long = 0L
)
