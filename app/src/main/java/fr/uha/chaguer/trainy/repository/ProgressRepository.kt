package fr.uha.chaguer.trainy.repository

import androidx.annotation.WorkerThread
import fr.uha.chaguer.trainy.database.ProgressDao
import fr.uha.chaguer.trainy.database.RoutineDao
import fr.uha.chaguer.trainy.database.RoutineProgressDao
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.model.Progress
import fr.uha.chaguer.trainy.model.RoutineProgress
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Date

class ProgressRepository(
    private val dispatcher: CoroutineDispatcher,
    private val progressDao: ProgressDao,
    private val routineProgressDao: RoutineProgressDao
) {

    suspend fun getCompletedExercisesForRoutine(routineId: Long): List<Long> =
        withContext(dispatcher) {
            progressDao.getCompletedExercises(routineId)
        }

    suspend fun addExerciseToProgress(routineId: Long, exerciseId: Long) =
        withContext(dispatcher) {
            val progress = Progress(
                routineId = routineId,
                exerciseId = exerciseId,
                isCompleted = true
            )
            progressDao.insertProgress(progress)
        }

    suspend fun removeExerciseFromProgress(routineId: Long, exerciseId: Long) =
        withContext(dispatcher) {
            progressDao.deleteProgress(routineId, exerciseId)
        }

    fun getProgressForRoutine(routineId: Long): Flow<List<RoutineProgress>> {
        return routineProgressDao.getProgressForRoutine(routineId)
    }

    suspend fun getProgressForExercise(routineId: Long, exerciseId: Long): RoutineProgress? {
        return routineProgressDao.getProgressForExercise(routineId, exerciseId)
    }

    suspend fun saveProgress(routineId: Long, exerciseId: Long, repetitions: Int, duration: Int) {
        val existingProgress = getProgressForExercise(routineId, exerciseId)

        val updatedProgress = if (existingProgress != null) {
            existingProgress.copy(
                completedRepetitions = existingProgress.completedRepetitions + repetitions,
                completedDuration = existingProgress.completedDuration + duration,
                date = Date()
            )
        } else {
            RoutineProgress(
                routineId = routineId,
                exerciseId = exerciseId,
                completedRepetitions = repetitions,
                completedDuration = duration,
                date = Date()
            )
        }

        routineProgressDao.insertOrUpdateProgress(updatedProgress)
    }

    suspend fun getExercise(exerciseId: Long): Exercise? {
        return routineProgressDao.getExerciseById(exerciseId)
    }

    @WorkerThread
    suspend fun getExerciseNameById(exerciseId: Long): String? = withContext(dispatcher) {
        routineProgressDao.getExerciseNameById(exerciseId)
    }
}