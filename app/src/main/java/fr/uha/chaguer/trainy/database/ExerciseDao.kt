package fr.uha.chaguer.trainy.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.model.Routine
import fr.uha.chaguer.trainy.model.RoutineExerciseAssociation
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises ORDER BY name ASC")
    fun getAllExercises(): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE exerciseId = :exerciseId")
    fun getExerciseById(exerciseId: Long): Flow<Exercise?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createExercise(exercise: Exercise) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateExercise(exercise: Exercise) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert (exercise: Exercise) : Long

    @Query("UPDATE exercises SET name = :newName WHERE exerciseId = :exerciseId")
    suspend fun updateExerciseName(exerciseId: Long, newName: String)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Insert
    suspend fun insertExercise(exercise: Exercise): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutineExerciseLink(link: RoutineExerciseAssociation)

    @Query("SELECT * FROM exercises INNER JOIN routine_exercise_associations ON exercises.exerciseId = routine_exercise_associations.exerciseId WHERE routineId = :routineId")
    fun getExercisesForRoutine(routineId: Long): Flow<List<Exercise>>

    @Query("DELETE FROM routine_exercise_associations WHERE routineId = :routineId AND exerciseId = :exerciseId")
    suspend fun removeExerciseFromRoutine(routineId: Long, exerciseId: Long)
}