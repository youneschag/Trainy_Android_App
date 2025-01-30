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

    fun getRoutineById(id: Long): Flow<FullRoutine?> {
        return routineDao.getRoutineById(id)
    }

    @WorkerThread
    suspend fun create(routine: Routine): Long = withContext(dispatcher) {
        return@withContext routineDao.createRoutine(routine)
    }

    @WorkerThread
    suspend fun updateRoutine(update: RoutineUpdateDTO) = withContext(dispatcher) {
        when (update) {
            is RoutineUpdateDTO.Name -> routineDao.update(update)
            is RoutineUpdateDTO.Frequency -> routineDao.update(update)
            is RoutineUpdateDTO.Objectives -> routineDao.update(update)
            is RoutineUpdateDTO.StartDay -> routineDao.update(update)
        }
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
    suspend fun addExerciseToRoutine(routineId: Long, exercise: Exercise) = withContext(dispatcher) {
        routineDao.addExerciseToRoutine(RoutineExerciseAssociation(routineId, exercise.exerciseId))
    }

    @WorkerThread
    suspend fun removeExercise(routineId: Long, exercise: Exercise) = withContext(dispatcher) {
        routineDao.removeExerciseFromRoutine(RoutineExerciseAssociation(routineId, exercise.exerciseId))
    }

    fun getAllFullRoutines(): Flow<List<FullRoutine>> {
        return routineDao.getAllFullRoutines()
    }

    fun getRoutineWithExercises(routineId: Long): Flow<FullRoutine?> {
        return routineDao.getRoutineWithExercises(routineId)
    }

}
