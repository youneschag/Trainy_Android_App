package fr.uha.chaguer.trainy.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "routineProgress")
data class RoutineProgress(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val routineId: Long,
    val exerciseId: Long,
    val completedRepetitions: Int,
    val completedDuration: Int,
    val date: Date,
)