package bo.bordadoxdanny.app.features.reports.domain

sealed class Period {
    abstract val id: String

    data object AllMonths : Period() {
        override val id: String = "ALL"
    }

    data class Year(val year: Int) : Period() {
        override val id: String = year.toString()
    }

    data class Month(val year: Int, val month: Int) : Period() {
        override val id: String = "$year-${month.toString().padStart(2, '0')}"
    }
}
