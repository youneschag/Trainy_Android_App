package fr.uha.chaguer.trainy.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.model.FullRoutine
import fr.uha.chaguer.trainy.model.Routine
import fr.uha.chaguer.trainy.model.RoutineExerciseAssociation
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface RoutineDao {
    @Query("SELECT * FROM routines ORDER BY name ASC")
    fun getAllRoutines(): Flow<List<Routine>>

    @Query("DELETE FROM routines")
    suspend fun deleteAllRoutines()

    @Transaction
    @Query("SELECT * FROM routines WHERE routineId = :routineId")
    fun getRoutineById(routineId: Long): Flow<Routine?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createRoutine(routine: Routine)  : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(routine: Routine): Long

    @Query("DELETE FROM routines WHERE routineId = :routineId")
    suspend fun deleteRoutine(routineId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExerciseToRoutine(association: RoutineExerciseAssociation)

    @Delete
    suspend fun removeExerciseFromRoutine(association: RoutineExerciseAssociation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRoutine(routine: Routine) : Long

    @Query("UPDATE routines SET name = :newName WHERE routineId = :routineId")
    suspend fun updateName(routineId: Long, newName: String)

    @Query("UPDATE routines SET startDay = :newStartDay WHERE routineId = :routineId")
    suspend fun updateStartDay(routineId: Long, newStartDay: Date)

    @Query("UPDATE routines SET frequency = :newFrequency WHERE routineId = :routineId")
    suspend fun updateFrequency(routineId: Long, newFrequency: Int)

    @Query("UPDATE routines SET objective = :newObjective WHERE routineId = :routineId")
    suspend fun updateObjective(routineId: Long, newObjective: String)

    @Query("""UPDATE exercises SET name = :newName WHERE exerciseId = :exerciseId AND exerciseId IN ( SELECT exerciseId FROM routine_exercise_associations WHERE routineId = :routineId)""")
    suspend fun updateExerciseName(routineId: Long, exerciseId: Long, newName: String)
}