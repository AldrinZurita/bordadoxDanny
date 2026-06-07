package bo.bordadoxdanny.app.features.reports.domain

data class AccountsReceivableItem(
    val id: String,
    val clientName: String,
    val description: String,
    val amount: Double,
    val isUrgent: Boolean
)
