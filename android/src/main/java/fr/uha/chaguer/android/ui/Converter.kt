package fr.uha.chaguer.android.ui

import fr.uha.chaguer.android.ui.field.Time
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class Converter {

    companion object {

        private var daySDF: SimpleDateFormat? = null
        private var timeSDF: SimpleDateFormat? = null

        private fun getDateShortFormatter(): SimpleDateFormat {
            if (daySDF == null) {
                daySDF = SimpleDateFormat("dd MMMM yyyy", Locale.FRANCE)
            }
            return daySDF!!
        }

        private fun getTimeShortFormatter(): SimpleDateFormat {
            if (timeSDF == null) {
                timeSDF = SimpleDateFormat("HH'h' mm'm'", Locale.FRANCE)
            }
            return timeSDF!!
        }

        fun convert(instant: Instant?, default: String = ""): String {
            return if (instant == null) {
                default
            } else {
                getDateShortFormatter().format(Date.from(instant))
            }
        }

        private const val DAY_IN_SECONDS: Int = 24 * 60 * 60
        private const val HOUR_IN_SECONDS: Int = 60 * 60
        private const val MINUTE_IN_SECONDS: Int = 60

        fun convertDuration(reference: Date?, current: Date): String {
            if (reference == null) return "? s"
            val elapsed = (current.time - reference.time) / 1000
            var remain = elapsed.toInt()
            val days = remain / DAY_IN_SECONDS
            remain -= days * DAY_IN_SECONDS
            val hours = remain / HOUR_IN_SECONDS
            remain -= hours * HOUR_IN_SECONDS
            val minutes = remain / MINUTE_IN_SECONDS
            remain -= minutes * MINUTE_IN_SECONDS
            val seconds = remain
            return when {
                days > 1 -> "$days d"
                days == 1 -> "$days d $hours h"
                hours != 0 -> "$hours h $minutes m"
                minutes != 0 -> "$minutes m $seconds s"
                seconds > 9 -> "$seconds s"
                seconds != 0 -> "a few seconds"
                else -> "now"
            }
        }

        fun convert(date: Date?, default: String = ""): String {
            return if (date == null) {
                default
            } else {
                getDateShortFormatter().format(date)
            }
        }

        fun convert(time: Time?, default: String = ""): String {
            return if (time == null) {
                default
            } else {
                val trimmedDay = trimToMidnight(time.reference)
                val offset: Long = (time.hour * 3600L + time.minute * 60L) * 1000L
                getTimeShortFormatter().format(Date(trimmedDay.time + offset))
            }
        }

        fun trimToMidnight(day: Date): Date {
            val calendar = GregorianCalendar()
            calendar.time = day
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            return calendar.time
        }

        fun safeStringToInt(value: String): Int? {
            return if (value.isEmpty()) null else try { value.toInt() } catch (e: Exception) { null }
        }

    }
}