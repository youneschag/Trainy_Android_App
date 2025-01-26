package fr.uha.chaguer.trainy.repository

import androidx.annotation.WorkerThread
import fr.uha.chaguer.trainy.database.RoutineDao
import fr.uha.chaguer.trainy.database.RoutineUpdateDTO
import fr.uha.chaguer.trainy.model.FullRoutine
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.model.Routine
import fr.uha.chaguer.trainy.model.RoutineExerciseAssociation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Date

class RoutineRepository(
    private val dispatcher: CoroutineDispatcher,
    private val routineDao: RoutineDao
) {

    fun getAll(): Flow<List<Routine>> {
        return routineDao.getAllRoutines()
    }

    fun getRoutineById(id: Long): Flow<Routine?> {
        return routineDao.getRoutineById(id)
    }

    @WorkerThread
    suspend fun create(routine: Routine): Long = withContext(dispatcher) {
        return@withContext routineDao.createRoutine(routine)
    }

    @WorkerThread
    suspend fun updateName(routineId: Long, newName: String) = withContext(dispatcher) {
        routineDao.updateName(routineId, newName)
    }

    @WorkerThread
    suspend fun updateStartDay(routineId: Long, newStartDay: Date) = withContext(dispatcher) {
        routineDao.updateStartDay(routineId, newStartDay)
    }

    @WorkerThread
    suspend fun updateFrequency(routineId: Long, newFrequency: Int) = withContext(dispatcher) {
        routineDao.updateFrequency(routineId, newFrequency)
    }

    @WorkerThread
    suspend fun updateObjective(routineId: Long, newObjective: String) = withContext(dispatcher) {
        routineDao.updateObjective(routineId, newObjective)
    }

    @WorkerThread
    suspend fun updateExerciseName(routineId: Long, exerciseId: Long, newName: String) = withContext(dispatcher) {
        routineDao.updateExerciseName(routineId, exerciseId, newName)
    }

    @WorkerThread
    suspend fun updateRoutine(routine: Routine) = withContext(dispatcher) {
        routineDao.updateRoutine(routine)
    }

    @WorkerThread
    suspend fun upsert(routine: Routine): Long = withContext(dispatcher) {
        return@withContext routineDao.upsert(routine)
    }

    @WorkerThread
    suspend fun deleteRoutine(routineId: Long) = withContext(dispatcher) {
        routineDao.deleteRoutine(routineId)
    }

    @WorkerThread
    suspend fun deleteAllRoutines() = withContext(dispatcher) {
        routineDao.deleteAllRoutines()
    }

    @WorkerThread
    suspend fun addExercise(routineId: Long, exercise: Exercise) = withContext(dispatcher) {
        routineDao.addExerciseToRoutine(RoutineExerciseAssociation(routineId, exercise.exerciseId))
    }

    @WorkerThread
    suspend fun removeExercise(routineId: Long, exerciseId: Long) = withContext(dispatcher) {
        routineDao.removeExerciseFromRoutine(RoutineExerciseAssociation(routineId, exerciseId))
    }
}
