package fr.uha.chaguer.trainy.repository

import androidx.annotation.WorkerThread
import fr.uha.chaguer.trainy.database.RoutineDao
import fr.uha.chaguer.trainy.database.RoutineProgressDao
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.model.RoutineProgress
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Date

class ProgressRepository(
    private val dispatcher: CoroutineDispatcher,
    private val routineProgressDao: RoutineProgressDao
) {

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
                id = 0,
                routineId = routineId,
                exerciseId = exerciseId,
                completedRepetitions = repetitions,
                completedDuration = duration,
                date = Date()
            )
        }

        routineProgressDao.insertOrUpdateProgress(updatedProgress)
    }

    suspend fun getExercise(id: Long): Exercise? {
        return routineProgressDao.getExerciseById(id)
    }

    @WorkerThread
    suspend fun getExerciseNameById(exerciseId: Long): String? = withContext(dispatcher) {
        routineProgressDao.getExerciseNameById(exerciseId)
    }
}