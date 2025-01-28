package fr.uha.chaguer.trainy.ui.progress

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.chaguer.trainy.model.RoutineProgress
import fr.uha.chaguer.trainy.repository.ProgressRepository
import fr.uha.chaguer.trainy.repository.RoutineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val repository: ProgressRepository
) : ViewModel() {

    suspend fun getCompletedExercises(routineId: Long): List<Long> {
        return repository.getCompletedExercisesForRoutine(routineId)
    }

    suspend fun saveProgress(routineId: Long, exerciseId: Long, isCompleted: Boolean) {
        if (isCompleted) {
            repository.addExerciseToProgress(routineId, exerciseId)
        } else {
            repository.removeExerciseFromProgress(routineId, exerciseId)
        }
    }

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
        // Récupérer les progrès existants
        val exerciseProgress = repository.getProgressForExercise(routineId, exerciseId)
        val newRepetitions = (exerciseProgress?.completedRepetitions ?: 0) + repetitions
        val newDuration = (exerciseProgress?.completedDuration ?: 0) + duration

        // Récupérer les données de l'exercice
        val targetExercise = repository.getExercise(exerciseId)
            ?: return ValidationResult(
                isSuccess = false,
                message = "Exercice introuvable."
            )

        // Validation des limites
        if (newRepetitions > targetExercise.repetitions || newDuration > targetExercise.duration) {
            return ValidationResult(
                isSuccess = false,
                message = "Vous ne pouvez pas dépasser les objectifs de l'exercice (${targetExercise.repetitions} répétitions, ${targetExercise.duration} minutes)."
            )
        }

        // Enregistrer les progrès
        repository.saveProgress(routineId, exerciseId, repetitions, duration)
        return ValidationResult(isSuccess = true)
    }
}

data class ValidationResult(
    val isSuccess: Boolean,
    val message: String = ""
)