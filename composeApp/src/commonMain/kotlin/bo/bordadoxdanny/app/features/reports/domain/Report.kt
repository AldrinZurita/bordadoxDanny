package bo.bordadoxdanny.app.features.reports.domain

data class Report(
    val id: Long = 0,
    val title: String,
    val content: String,
    val createdDate: Long
)
