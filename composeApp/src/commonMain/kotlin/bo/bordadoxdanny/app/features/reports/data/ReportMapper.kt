package bo.bordadoxdanny.app.features.reports.data

import bo.bordadoxdanny.app.features.reports.domain.FinancialSummary
import bo.bordadoxdanny.app.features.reports.domain.Period
import bo.bordadoxdanny.app.features.reports.domain.Report

fun ReportSummaryEntity.toDomain(): FinancialSummary {
    val period = when {
        year == null && month == null -> Period.AllMonths
        month == null -> Period.Year(year!!)
        else -> Period.Month(year!!, month!!)
    }
    return FinancialSummary(
        totalIncome = totalIncome,
        totalExpenses = totalExpenses,
        netProfit = netProfit,
        accountsReceivable = accountsReceivable,
        period = period
    )
}

fun FinancialSummary.toEntity(): ReportSummaryEntity {
    return ReportSummaryEntity(
        periodId = period.id,
        totalIncome = totalIncome,
        totalExpenses = totalExpenses,
        netProfit = netProfit,
        accountsReceivable = accountsReceivable,
        year = when (val p = period) {
            is Period.Year -> p.year
            is Period.Month -> p.year
            else -> null
        },
        month = when (val p = period) {
            is Period.Month -> p.month
            else -> null
        }
    )
}

fun ReportSummaryDto.toEntity() = ReportSummaryEntity(
    periodId = periodId,
    totalIncome = totalIncome,
    totalExpenses = totalExpenses,
    netProfit = netProfit,
    accountsReceivable = accountsReceivable,
    year = year,
    month = month,
    syncStatus = "SYNCED",
    updatedAt = updatedAt
)

fun ReportSummaryEntity.toDto() = ReportSummaryDto(
    periodId = periodId,
    totalIncome = totalIncome,
    totalExpenses = totalExpenses,
    netProfit = netProfit,
    accountsReceivable = accountsReceivable,
    year = year,
    month = month,
    updatedAt = updatedAt
)

fun ReportEntity.toDomain() = Report(
    id = id,
    title = title,
    content = content,
    createdDate = createdDate
)
