package fr.uha.chaguer.trainy.model

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "routine_exercise_associations",
    primaryKeys = ["routineId", "exerciseId"],
    indices = [Index("routineId"), Index("exerciseId")]
)
data class RoutineExerciseAssociation(
    val routineId: Long,
    val exerciseId: Long
)