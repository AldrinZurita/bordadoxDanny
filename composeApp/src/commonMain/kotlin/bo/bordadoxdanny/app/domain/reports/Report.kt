package bo.bordadoxdanny.app.domain.reports

data class Report(
    val id: Long = 0,
    val title: String,
    val content: String,
    val createdDate: Long
)
