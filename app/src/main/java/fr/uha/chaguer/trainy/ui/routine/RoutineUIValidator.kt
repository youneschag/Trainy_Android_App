package fr.uha.chaguer.trainy.ui.routine

import fr.uha.chaguer.trainy.R
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.model.FullRoutine
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

class RoutineUIValidator(private val routine: FullRoutine) {

    fun validateName(newValue: String): Int? {
        return when {
            newValue.isEmpty() -> R.string.value_empty
            newValue.isBlank() -> R.string.value_blank
            newValue.length < 3 -> R.string.value_too_short
            else -> null
        }
    }

    private fun instantToMidnight(date: Date): Instant {
        val calendar = GregorianCalendar()
        calendar.time = date
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR, 0)
        return calendar.toInstant()
    }

    fun validateStartDay(newValue: Date?): Int? {
        if (newValue == null) return R.string.date_must_set
        val day = instantToMidnight(newValue)
        val today = instantToMidnight(Date())
        val between: Long = ChronoUnit.DAYS.between(today, day)
        if (between < -30) return R.string.date_too_old
        if (between > 365) return R.string.date_too_far
        return null
    }

    fun validateFrequency(newValue: Int): Int? {
        return when {
            newValue < 1 -> R.string.frequency_too_low
            newValue > 7 -> R.string.frequency_too_high
            else -> null
        }
    }

    fun validateObjective(newValue: String): Int? {
        return when {
            newValue.isEmpty() -> R.string.value_empty
            newValue.isBlank() -> R.string.value_blank
            newValue.length < 5 -> R.string.value_too_short
            else -> null
        }
    }

    fun validateExercises(newValue: List<Exercise>): Int? {
        return if (newValue.isEmpty()) R.string.exercises_empty else null
    }
}