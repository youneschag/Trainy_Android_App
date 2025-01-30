package fr.uha.chaguer.trainy.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.uha.chaguer.trainy.model.Exercise
import fr.uha.chaguer.trainy.model.RoutineProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineProgressDao {

    @Query("SELECT * FROM RoutineProgress WHERE routineId = :routineId")
    fun getProgressForRoutine(routineId: Long): Flow<List<RoutineProgress>>

    @Query("SELECT * FROM RoutineProgress WHERE routineId = :routineId AND exerciseId = :exerciseId")
    suspend fun getProgressForExercise(routineId: Long, exerciseId: Long): RoutineProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(progress: RoutineProgress)

    @Query("SELECT * FROM exercises WHERE exerciseId = :exerciseId")
    suspend fun getExerciseById(exerciseId: Long): Exercise?

    @Query("SELECT name FROM exercises WHERE exerciseId = :exerciseId")
    fun getExerciseNameById(exerciseId: Long): String?
}