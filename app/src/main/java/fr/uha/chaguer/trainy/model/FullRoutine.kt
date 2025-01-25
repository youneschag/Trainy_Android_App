package fr.uha.chaguer.trainy.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class FullRoutine(
    @Embedded
    val routine: Routine,

    @Relation(parentColumn = "routineId", entityColumn = "exerciseId", associateBy = Junction(RoutineExerciseAssociation::class))
    val exercises: List<Exercise> = emptyList()
)