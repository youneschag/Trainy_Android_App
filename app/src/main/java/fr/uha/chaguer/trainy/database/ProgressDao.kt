package fr.uha.chaguer.trainy.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.uha.chaguer.trainy.model.Progress

@Dao
interface ProgressDao {
    @Query("SELECT exerciseId FROM progress WHERE routineId = :routineId AND isCompleted = 1")
    fun getCompletedExercises(routineId: Long): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProgress(progress: Progress)

    @Query("DELETE FROM progress WHERE routineId = :routineId AND exerciseId = :exerciseId")
    fun deleteProgress(routineId: Long, exerciseId: Long)
}

