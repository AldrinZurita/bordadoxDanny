package bo.bordadoxdanny.app.core.designsystem.accessibility

object A11yFormatters {

    /**
     * Converts a raw snake_case or camelCase identifier into a
     * human-readable label for TalkBack.
     */
    fun identifierToLabel(raw: String): String {
        val spaced = raw
            .replace(Regex("([a-z])([A-Z])"), "$1 $2")
            .replace(Regex("[_\\-]+"), " ")
            .trim()
        return spaced.replaceFirstChar { it.uppercaseChar() }
    }

    /**
     * Sanitizes a user-supplied string for use in a contentDescription.
     */
    fun sanitizeUserInput(raw: String): String =
        raw.replace(Regex("[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]"), " ")
           .replace(Regex("\\s{2,}"), " ")
           .trim()

    /**
     * Formats a numeric value with a unit into a speakable phrase.
     */
    fun formatMetric(value: String, unit: String): String {
        val clean = value.replace(Regex("[^0-9]"), "")
        return if (clean.isEmpty()) "Unknown $unit" else "$clean $unit"
    }

    /**
     * Converts an ISO date string into a speakable date description.
     */
    fun formatDate(raw: String): String {
        val match = Regex("""(\d{4})-(\d{2})-(\d{2})""").find(raw) ?: return raw
        val (year, month, day) = match.destructured
        val monthName = when (month) {
            "01" -> "January";  "02" -> "February"; "03" -> "March"
            "04" -> "April";    "05" -> "May";       "06" -> "June"
            "07" -> "July";     "08" -> "August";    "09" -> "September"
            "10" -> "October";  "11" -> "November";  "12" -> "December"
            else -> month
        }
        return "$monthName $day, $year"
    }

    /**
     * Formats a handle for TalkBack.
     */
    fun formatHandle(raw: String): String =
        sanitizeUserInput(raw.removePrefix("@"))

    /**
     * Returns a spoken description of a URL.
     */
    fun formatUrl(raw: String): String {
        val domain = Regex("""https?://([^/]+)""").find(raw)?.groupValues?.get(1)
        return if (domain != null) "Link to $domain" else "External link"
    }
}
