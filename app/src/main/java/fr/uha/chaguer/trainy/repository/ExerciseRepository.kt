package fr.uha.chaguer.trainy.repository

import androidx.annotation.WorkerThread
import fr.uha.chaguer.trainy.database.ExerciseDao
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.model.RoutineExerciseAssociation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ExerciseRepository(
    private val dispatcher: CoroutineDispatcher,
    private val exerciseDao: ExerciseDao
) {

    fun getAllExercises(): Flow<List<Exercise>> {
        return exerciseDao.getAllExercises()
    }

    fun getExerciseById(id: Long): Flow<Exercise?> {
        return exerciseDao.getExerciseById(id)
    }

    @WorkerThread
    suspend fun createExercise(exercise: Exercise): Long = withContext(dispatcher) {
        return@withContext exerciseDao.createExercise(exercise)
    }

    @WorkerThread
    suspend fun updateExercise(exercise: Exercise) = withContext(dispatcher) {
        exerciseDao.updateExercise(exercise)
    }

    @WorkerThread
    suspend fun upsertExercise(exercise: Exercise): Long = withContext(dispatcher) {
        return@withContext exerciseDao.upsert(exercise)
    }

    @WorkerThread
    suspend fun addExerciseToRoutine(routineId: Long, exercise: Exercise) {
        val exerciseId = exerciseDao.insertExercise(exercise)
        exerciseDao.insertRoutineExerciseLink(
            RoutineExerciseAssociation(routineId = routineId, exerciseId = exerciseId)
        )
    }

    @WorkerThread
    suspend fun deleteExerciseFromRoutine(routineId: Long, exerciseId: Long) {
        exerciseDao.removeExerciseFromRoutine(routineId, exerciseId)
    }

    @WorkerThread
    suspend fun addExercise(exercise: Exercise) = withContext(dispatcher) {
        exerciseDao.insertExercise(exercise)
    }

    @WorkerThread
    suspend fun updateExerciseName(exerciseId: Long, newName: String) = withContext(dispatcher) {
        exerciseDao.updateExerciseName(exerciseId, newName)
    }

    @WorkerThread
    suspend fun deleteExercise(exercise: Exercise) = withContext(dispatcher) {
        exerciseDao.deleteExercise(exercise)
    }
}