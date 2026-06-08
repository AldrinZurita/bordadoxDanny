package bo.bordadoxdanny.app.features.reports.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "report_summaries")
data class ReportSummaryEntity(
    @PrimaryKey val periodId: String, // e.g., "ALL", "2026", "2026-06"
    val totalIncome: Double,
    val totalExpenses: Double,
    val netProfit: Double,
    val accountsReceivable: Double,
    val year: Int?,
    val month: Int?,
    val syncStatus: String = "PENDING",
    val updatedAt: Long = 0L
)
