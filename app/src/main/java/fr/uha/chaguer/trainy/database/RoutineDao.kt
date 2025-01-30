package fr.uha.chaguer.trainy.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import fr.uha.chaguer.trainy.model.FullRoutine
import fr.uha.chaguer.trainy.model.Routine
import fr.uha.chaguer.trainy.model.RoutineExerciseAssociation
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Query("SELECT * FROM routines ORDER BY name ASC")
    fun getAllRoutines(): Flow<List<Routine>>

    @Query("SELECT * FROM routines WHERE routineId = :routineId")
    fun getRoutineById(routineId: Long): Flow<FullRoutine?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createRoutine(routine: Routine) : Long

    @Update(entity = Routine::class)
    suspend fun update(routine: RoutineUpdateDTO.Name)

    @Update(entity = Routine::class)
    suspend fun update(routine: RoutineUpdateDTO.StartDay)

    @Update(entity = Routine::class)
    suspend fun update(routine: RoutineUpdateDTO.Frequency)

    @Update(entity = Routine::class)
    suspend fun update(routine: RoutineUpdateDTO.Objectives)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(routine: Routine) : Long

    @Query("DELETE FROM routines WHERE routineId = :routineId")
    suspend fun deleteRoutine(routineId: Long)

    @Query("DELETE FROM routines")
    suspend fun deleteAllRoutines()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addExerciseToRoutine(association: RoutineExerciseAssociation)

    @Delete
    suspend fun removeExerciseFromRoutine(association: RoutineExerciseAssociation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRoutine(routine: Routine) : Long

    @Query("""UPDATE exercises SET name = :newName WHERE exerciseId = :exerciseId AND exerciseId IN ( SELECT exerciseId FROM routine_exercise_associations WHERE routineId = :routineId)""")
    suspend fun updateExerciseName(routineId: Long, exerciseId: Long, newName: String)

    @Transaction
    @Query("SELECT * FROM routines")
    fun getAllFullRoutines(): Flow<List<FullRoutine>>

    @Transaction
    @Query("SELECT * FROM routines WHERE routineId = :routineId")
    fun getRoutineWithExercises(routineId: Long): Flow<FullRoutine?>

}