package fr.uha.chaguer.trainy.ui.progress

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.chaguer.trainy.model.RoutineProgress
import fr.uha.chaguer.trainy.repository.ProgressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val repository: ProgressRepository
) : ViewModel() {

    fun getProgressForRoutine(routineId: Long): Flow<List<RoutineProgress>> {
        return repository.getProgressForRoutine(routineId)
    }

    suspend fun getExerciseNameById(exerciseId: Long): String {
        return withContext(Dispatchers.IO) {
            repository.getExerciseNameById(exerciseId) ?: "Exercice inconnu"
        }
    }

    suspend fun saveProgressWithValidation(
        routineId: Long,
        exerciseId: Long,
        repetitions: Int,
        duration: Int
    ): ValidationResult {
        val exerciseProgress = repository.getProgressForExercise(routineId, exerciseId)
        val newRepetitions = (exerciseProgress?.completedRepetitions ?: 0) + repetitions
        val newDuration = (exerciseProgress?.completedDuration ?: 0) + duration

        val targetExercise = repository.getExercise(exerciseId)
            ?: return ValidationResult(
                isSuccess = false,
                message = "Exercice introuvable."
            )

        if (newRepetitions > targetExercise.repetitions || newDuration > targetExercise.duration) {
            return ValidationResult(
                isSuccess = false,
                message = "Vous ne pouvez pas dépasser les objectifs de l'exercice (${targetExercise.repetitions} répétitions, ${targetExercise.duration} minutes)."
            )
        }

        repository.saveProgress(routineId, exerciseId, repetitions, duration)
        return ValidationResult(isSuccess = true)
    }
}

data class ValidationResult(
    val isSuccess: Boolean,
    val message: String = ""
)