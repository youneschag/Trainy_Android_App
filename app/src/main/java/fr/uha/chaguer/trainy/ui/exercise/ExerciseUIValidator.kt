package fr.uha.chaguer.trainy.ui.exercise

import fr.uha.chaguer.trainy.R
import kotlinx.coroutines.flow.StateFlow
import fr.uha.chaguer.android.viewmodel.Result

class ExerciseUIValidator(private val uiState: StateFlow<Result<ExerciseViewModel.UIState>>) {

    fun validateNameChange(newValue: String): Int? {
        return when {
            newValue.isEmpty() -> R.string.value_empty
            newValue.isBlank() -> R.string.value_blank
            newValue.length < 3 -> R.string.value_too_short
            newValue.length > 50 -> R.string.value_too_long
            else -> null
        }
    }

    fun validateDescriptionChange(newValue: String): Int? {
        return when {
            newValue.isEmpty() -> R.string.value_empty
            newValue.isBlank() -> R.string.value_blank
            newValue.length < 10 -> R.string.value_too_short
            newValue.length > 200 -> R.string.value_too_long
            else -> null
        }
    }

    fun validateDurationChange(newValue: Int): Int? {
        return when {
            newValue <= 0 -> R.string.value_must_be_positive
            newValue > 300 -> R.string.value_too_long
            else -> null
        }
    }

    fun validateRepetitionsChange(newValue: Int): Int? {
        return when {
            newValue <= 0 -> R.string.value_must_be_positive
            newValue > 1000 -> R.string.value_too_large
            else -> null
        }
    }
}