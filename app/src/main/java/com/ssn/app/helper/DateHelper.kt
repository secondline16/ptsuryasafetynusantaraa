package com.ssn.app.helper

import java.text.SimpleDateFormat
import java.util.Locale

object DateHelper {

    const val API_DATE_PATTERN = "yyyy-MM-dd"
    const val VIEW_DATE_PATTERN = "dd MMMM yyyy"

    fun changeFormat(
        dateString: String,
        oldFormat: String,
        newFormat: String,
        default: String = dateString
    ): String = try {
        val oldFormatter = SimpleDateFormat(oldFormat, Locale.getDefault())
        val oldDate = oldFormatter.parse(dateString)
        oldDate?.let { date ->
            val newFormatter = SimpleDateFormat(newFormat, Locale.getDefault())
            newFormatter.format(date)
        } ?: run {
            default
        }
    } catch (e: Exception) {
        default
    }
}
