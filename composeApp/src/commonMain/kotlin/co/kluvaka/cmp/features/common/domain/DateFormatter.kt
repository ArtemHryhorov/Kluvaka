package co.kluvaka.cmp.features.common.domain

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateFormatter {
    fun format(timestamp: Long, pattern: String = "d MMMM yyyy"): String {
        if (timestamp <= 0) return ""
        
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        
        // Basic implementation for the requested default pattern "d MMMM yyyy"
        // For a full implementation, a proper date formatting library or platform specific code is needed.
        // Here we handle the requested default explicitly.
        
        return if (pattern == "d MMMM yyyy") {
            val monthName = when (localDate.monthNumber) {
                1 -> "January"
                2 -> "February"
                3 -> "March"
                4 -> "April"
                5 -> "May"
                6 -> "June"
                7 -> "July"
                8 -> "August"
                9 -> "September"
                10 -> "October"
                11 -> "November"
                12 -> "December"
                else -> ""
            }
            "${localDate.dayOfMonth} $monthName ${localDate.year}"
        } else {
            // Fallback for other patterns (simplified, can be expanded)
            // For now, just return ISO format if pattern is not the default one
             "${localDate.year}-${localDate.monthNumber.toString().padStart(2, '0')}-${localDate.dayOfMonth.toString().padStart(2, '0')}"
        }
    }
}
